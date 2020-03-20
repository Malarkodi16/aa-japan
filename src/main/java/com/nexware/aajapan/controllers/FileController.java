package com.nexware.aajapan.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.StockDetailsDto;
import com.nexware.aajapan.models.TPurchaseInvoice;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.payload.UploadFileResponse;
import com.nexware.aajapan.property.FileStorageProperties;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TPurchaseInvoiceRepository;
import com.nexware.aajapan.services.FileStorageService;
import com.nexware.aajapan.services.S3Factory;
import com.nexware.aajapan.utils.AppUtil;

import constants.FbFeedConstants;

@RestController
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private MongoOperations mongoOperation;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	@Autowired
	private TPurchaseInvoiceRepository purchaseInvoiceRepository;
	@Autowired
	private S3Factory s3FactoryService;

	@PostMapping("/file/upload")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("directory") String directory, @RequestParam("filename") String filename) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		final String diskFileName = fileStorageService.storeFile(file, directory, "upload");

		final String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.queryParam("path", directory).queryParam("from", "upload").path(fileName).toUriString();

		return new UploadFileResponse(fileName, diskFileName, directory, fileDownloadUri, file.getContentType(),
				file.getSize());

	}

	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("directory") String directory, @RequestParam("subDirectory") String subDirectory,
			@RequestParam("uploadDirectory") String uploadDirectory) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		directory = uploadDirectory.isEmpty() ? directory : uploadDirectory;

		final String diskFileName = fileStorageService.storeFile(file, directory + File.separator + subDirectory,
				uploadDirectory);

		final String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.queryParam("path", directory + File.separator + subDirectory)
				.queryParam("from", uploadDirectory.isEmpty() ? "temp" : "upload").path(fileName).toUriString();

		return new UploadFileResponse(fileName, diskFileName, directory, fileDownloadUri, file.getContentType(),
				file.getSize());
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @RequestParam("path") String path,
			@RequestParam("from") String from, HttpServletRequest request) {
		// Load file as Resource
		final Resource resource = fileStorageService.loadFileAsResource(path + File.separator + fileName, from);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (final IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/get/{fileName:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileName, @RequestParam("path") String path,
			@RequestParam("from") String from, @RequestParam(value = "invoiceNo", required = false) String invoiceNo,
			HttpServletRequest request) {
		// Load file as Resource
		final Resource resource = fileStorageService.loadFileAsResource(path + File.separator + fileName, from);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (final IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		if (!AppUtil.isObjectEmpty(invoiceNo)) {
			final List<TPurchaseInvoice> invoices = purchaseInvoiceRepository.findAllByInvoiceNo(invoiceNo);
			for (final TPurchaseInvoice tPurchaseInvoice : invoices) {
				tPurchaseInvoice.setAttachementViewed(Constants.ATTACHMENT_VIEWED);
			}
			purchaseInvoiceRepository.saveAll(invoices);
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))

				.body(resource);
	}

	@DeleteMapping("/deleteFile")
	public ResponseEntity<HttpStatus> deleteFile(@RequestParam("filename") String filename,
			@RequestParam("directory") String directory, @RequestParam("subdirectory") String subdirectory,
			@RequestParam("from") String from, @RequestParam("id") String id, HttpServletRequest request)
			throws IOException {
		// Load file as Resource
		final Resource resource = fileStorageService
				.loadFileAsResource(directory + File.separator + subdirectory + File.separator + filename, from);
		Files.delete(resource.getFile().toPath());

		if (!id.isEmpty()) {
			final Update update = new Update().pull("attachments", Query.query(Criteria.where("_id").is(id)));
			mongoOperation.updateMulti(new Query(Criteria.where("stockNo").is(directory)), update, TStock.class);

		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@GetMapping(value = "/stock-image/zip/download", produces = "application/zip")
//	public void zipFiles(@RequestParam("stockNo") String stockNo, HttpServletResponse response) throws IOException {
//
//		// find stock
//		TStock stock = this.stockRepository.findOneByStockNo(stockNo);
//		// setting headers
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.addHeader("Content-Disposition", "attachment; filename=\"" + stock.getChassisNo() + ".zip\"");
//
//		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
//
//		// create a list to add files to be zipped
//		if (!AppUtil.isObjectEmpty(stock.getAttachments())) {
//			for (Attachment attachment : stock.getAttachments()) {
//				Path uploadFileStorageLocation = Paths.get(fileStorageProperties.getDirectory()).toAbsolutePath()
//						.normalize();
//				File stockImage = new File(uploadFileStorageLocation.toAbsolutePath().toString() + File.separator
//						+ attachment.getDiskDirectory() + File.separator + attachment.getSubDirectory() + File.separator
//						+ attachment.getDiskFilename());
//				zipOutputStream.putNextEntry(new ZipEntry(stockImage.getName()));
//				FileInputStream fileInputStream = new FileInputStream(stockImage);
//
//				IOUtils.copy(fileInputStream, zipOutputStream);
//
//				fileInputStream.close();
//				zipOutputStream.closeEntry();
//			}
//		}
//
//		zipOutputStream.close();
//	}

	@GetMapping(value = "/stock-image/zip/download", produces = "application/zip")
	public void zipFiles(@RequestParam("stockNo") String stockNo, HttpServletResponse response) throws IOException {

		// find stock
		StockDetailsDto stockDetailsDto = stockRepository.findOneStockDetailsByStockNo(stockNo);
		// setting headers
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition",
				"attachment; filename=\"" + stockDetailsDto.getChassisNo() + ".zip\"");

		List<S3ObjectSummary> listOfImageObject = s3FactoryService.getNoOfAttachment(stockDetailsDto.getChassisNo(),
				stockDetailsDto.getDestinationCountry(), stockDetailsDto.getSupplierName());

		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

		// create a list to add files to be zipped
		if (!AppUtil.isObjectEmpty(listOfImageObject)) {
			for (S3ObjectSummary image : listOfImageObject) {
				try {
					AmazonS3 s3Client = new AmazonS3Client();
					S3Object stockImageObject = s3Client
							.getObject(new GetObjectRequest(FbFeedConstants.AWSTOCK_IMAGE_BUCKET_NAME, image.getKey()));
					// get file from oject
					S3ObjectInputStream s3streamFile = stockImageObject.getObjectContent();
					zipOutputStream.putNextEntry(new ZipEntry(stockImageObject.getKey()));
					// copy stream file to zip
					IOUtils.copy(s3streamFile, zipOutputStream);
					// zipOutputStream flush and close connection
					zipOutputStream.flush();
					zipOutputStream.closeEntry();

				} catch (AmazonServiceException e) {
					// The call was transmitted successfully, but Amazon S3 couldn't process
					// it, so it returned an error response.
					e.printStackTrace();
					System.out.println("FIRST EXCEPTION" + e.getMessage());
				} catch (SdkClientException e) {
					// Amazon S3 couldn't be contacted for a response, or the client
					// couldn't parse the response from Amazon S3.
					e.printStackTrace();
					System.out.println("SECOND EXCEPTION" + e.getMessage());
				}

			}
		}

		zipOutputStream.close();
	}

}
