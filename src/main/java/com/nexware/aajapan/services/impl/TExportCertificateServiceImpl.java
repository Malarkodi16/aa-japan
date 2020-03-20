package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.TExportCertificate;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.repositories.TExportCertificateRepository;
import com.nexware.aajapan.services.TExportCertificateService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class TExportCertificateServiceImpl implements TExportCertificateService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TExportCertificateRepository exportCertificateRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	ObjectMapper mapper;

	@Override
	public void saveExportCertificate(TExportCertificate exportCertificate) {
		if (AppUtil.isObjectEmpty(exportCertificate)) {
			throw new AAJRuntimeException("Exception while completing payment");
		}
		TExportCertificate isExists = this.exportCertificateRepository.findOneByStockNo(exportCertificate.getStockNo());
		// set first reg. date
		Date firstRegDate = null;
		if (!AppUtil.isObjectEmpty(exportCertificate.getsFirstRegDate())) {
			firstRegDate = AppUtil.parseDate(exportCertificate.getsFirstRegDate().replaceAll("_+", ""));
		}

		if (AppUtil.isObjectEmpty(isExists)) {
			this.exportCertificateRepository.save(exportCertificate);
		} else {
			Update update = new Update().set("stockNo", exportCertificate.getStockNo())
					.set("registrationNo1", exportCertificate.getRegistrationNo1())
					.set("registrationNo2", exportCertificate.getRegistrationNo2())
					.set("registrationNo3", exportCertificate.getRegistrationNo3())
					.set("registrationNo4", exportCertificate.getRegistrationNo4())
					.set("registrationDate", exportCertificate.getRegistrationDate()).set("firstRegDate", firstRegDate)
					.set("sFirstRegDate", exportCertificate.getsFirstRegDate())
					.set("makerSerialNo", exportCertificate.getMakerSerialNo())
					.set("trademarkVehicle", exportCertificate.getTrademarkVehicle())
					.set("model", exportCertificate.getModelType())
					.set("engineModel", exportCertificate.getEngineModel())
					.set("classificationOfVehicle", exportCertificate.getClassificationOfVehicle())
					.set("use", exportCertificate.getUse()).set("purpose", exportCertificate.getPurpose())
					.set("typeOfBody", exportCertificate.getTypeOfBody())
					.set("fixedNumber", exportCertificate.getFixedNumber())
					.set("maximCarry", exportCertificate.getMaximCarry()).set("weight", exportCertificate.getWeight())
					.set("grossWeight", exportCertificate.getGrossWeight())
					.set("engineCapacity", exportCertificate.getEngineCapacity())
					.set("unit", exportCertificate.getUnit()).set("fuel", exportCertificate.getFuel())
					.set("specificationNo", exportCertificate.getSpecificationNo())
					.set("classificationNo", exportCertificate.getClassificationNo())
					.set("length", exportCertificate.getLength()).set("width", exportCertificate.getWidth())
					.set("height", exportCertificate.getHeight()).set("ffWeight", exportCertificate.getFfWeight())
					.set("frWeight", exportCertificate.getFrWeight()).set("rfWeight", exportCertificate.getRfWeight())
					.set("rrWeight", exportCertificate.getRrWeight())
					.set("exportScheduleDate", exportCertificate.getExportScheduleDate())
					.set("remark", exportCertificate.getRemark())
					.set("convertedDate", exportCertificate.getConvertedDate())
					.set("serialNo", exportCertificate.getSerialNo())
					.set("referenceNo", exportCertificate.getReferenceNo())
					.set("nameOfOwner", exportCertificate.getNameOfOwner())
					.set("addressOfOwner", exportCertificate.getAddressOfOwner())
					.set("nameOfUser", exportCertificate.getNameOfUser())
					.set("addressOfUser", exportCertificate.getAddressOfUser())
					.set("localityOfPrincipalOfUse", exportCertificate.getLocalityOfPrincipalOfUse());

			this.mongoTemplate.updateFirst(Query.query(Criteria.where("stockNo").is(exportCertificate.getStockNo())),
					update, TExportCertificate.class);
		}
		TStock stock = this.stockRepository.findOneByStockNo(exportCertificate.getStockNo());
		stock.setLength(exportCertificate.getLength());
		stock.setWidth(exportCertificate.getWidth());
		stock.setHeight(exportCertificate.getHeight());
		stock.calcM3();
		stock.setNoOfSeat(exportCertificate.getFixedNumber());
		stock.setsFirstRegDate(exportCertificate.getsFirstRegDate());
		
		this.stockRepository.save(stock);

	}

	@Override
	public void updateDocumentFobPrice(Map<String, Object> data) throws IOException, ParseException {
		List<TStock> stock = this.mapper.readValue(this.mapper.writeValueAsString(data.get("fobPriceList")),
				new TypeReference<List<TStock>>() {
				});

		List<TStock> stockList = new ArrayList<>();
		stock.stream().forEach(s -> {

			TStock stock1 = this.stockRepository.findOneByStockNo(s.getStockNo());
			stock1.setDocumentFob(s.getDocumentFob());
			stockList.add(stock1);
		});
		this.stockRepository.saveAll(stockList);

	}
}
