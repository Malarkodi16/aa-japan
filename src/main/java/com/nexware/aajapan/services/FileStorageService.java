package com.nexware.aajapan.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nexware.aajapan.exceptions.FileStorageException;
import com.nexware.aajapan.exceptions.MyFileNotFoundException;
import com.nexware.aajapan.property.FileStorageProperties;

@Service
public class FileStorageService {

	private final Path tempFileStorageLocation;
	private final Path uploadFileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.tempFileStorageLocation = Paths.get(fileStorageProperties.getTempDirectory()).toAbsolutePath().normalize();
		this.uploadFileStorageLocation = Paths.get(fileStorageProperties.getDirectory()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.tempFileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the temporary files will be stored.",
					ex);
		}
	}

	public String storeFile(MultipartFile file, String directory, String toUpload) {
		// Normalize file name

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		fileName = FilenameUtils.removeExtension(fileName) + "_" + System.currentTimeMillis() + "."
				+ FilenameUtils.getExtension(fileName);
		try {
			// Check if the file's name contains invalid characters
			Path path = toUpload.isEmpty() ? this.tempFileStorageLocation : this.uploadFileStorageLocation;
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			File directoryToUpload = new File(path.toAbsolutePath().toString() + File.separator + directory);
			if (!directoryToUpload.exists()) {
				directoryToUpload.mkdirs();
			}
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = path.resolve(directoryToUpload.getAbsolutePath() + File.separator + fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName, String fromDir) {
		try {

			Path filePath;
			if (fromDir.equalsIgnoreCase("temp")) {
				filePath = this.tempFileStorageLocation.resolve(fileName).normalize();
			} else {
				filePath = this.uploadFileStorageLocation.resolve(fileName).normalize();

			}
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	public String storeDaybookSlip(MultipartFile file, String filename, String directory, String toUpload) {
		// Normalize file name

		String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(), filename);
		try {
			// Check if the file's name contains invalid characters
			Path path = toUpload.isEmpty() ? this.tempFileStorageLocation : this.uploadFileStorageLocation;
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			File directoryToUpload = new File(path.toAbsolutePath().toString() + File.separator + directory);
			if (!directoryToUpload.exists()) {
				directoryToUpload.mkdirs();
			}
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = path.resolve(directoryToUpload.getAbsolutePath() + File.separator + fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

}
