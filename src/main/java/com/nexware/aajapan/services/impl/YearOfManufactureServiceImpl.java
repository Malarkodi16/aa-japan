package com.nexware.aajapan.services.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.TYearOfManufacture;
import com.nexware.aajapan.repositories.YearOfManufactureRepository;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.services.YearOfManufactureService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class YearOfManufactureServiceImpl implements YearOfManufactureService {

	@Autowired
	private YearOfManufactureRepository yearOfManufactureRepository;

	@Autowired
	private SequenceService sequenceService;
	
	@Autowired
	private MongoTemplate mongoTemplate;


	@Override
	public void saveManufactureYear(TYearOfManufacture tYearOfManufacture) {
		
		
		tYearOfManufacture.setDeleteFlag(Constants.DELETE_FLAG_0);
		if(tYearOfManufacture.getCode() != null) {
			TYearOfManufacture manuYearObj = this.yearOfManufactureRepository.findByCode(tYearOfManufacture.getCode());
			if(manuYearObj != null) {
				if(tYearOfManufacture.getMaker() != null ) manuYearObj.setMaker(tYearOfManufacture.getMaker());
				if(tYearOfManufacture.getModel() != null ) manuYearObj.setModel(tYearOfManufacture.getModel());
				if(tYearOfManufacture.getModelNo() != null ) manuYearObj.setModelNo(tYearOfManufacture.getModelNo());
				if(tYearOfManufacture.getFrame() != null ) manuYearObj.setFrame(tYearOfManufacture.getFrame());
				if(tYearOfManufacture.getFormatedSerialNoFrom() != null ) manuYearObj.setFormatedSerialNoFrom(tYearOfManufacture.getFormatedSerialNoFrom());
				if(tYearOfManufacture.getFormatedSerialNoTo() != null ) manuYearObj.setFormatedSerialNoTo(tYearOfManufacture.getFormatedSerialNoTo());
				if(tYearOfManufacture.getManufactureYear() != null ) manuYearObj.setManufactureYear(tYearOfManufacture.getManufactureYear());
				tYearOfManufacture = manuYearObj;
			}
		}else {
			tYearOfManufacture.setCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_YOMNFTR));
		}
		this.yearOfManufactureRepository.save(tYearOfManufacture);
	}

	@Override
	public Date yearOfManufacture(String stockFrame, String stockModelNo) {
		String modelNo = null;
		String frame = null;
		Long serialNo = null;
		TYearOfManufacture tYearOfManufacture = null;
		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		try {
			if (!AppUtil.isObjectEmpty(stockModelNo)) {
				Matcher stockModelNoMatch = pattern.matcher(stockModelNo);
				boolean b = stockModelNoMatch.find();
				if (b) {
					modelNo = stockModelNo.split("-")[0];
				}

			}
			if (!AppUtil.isObjectEmpty(stockFrame)) {
				Matcher stockFrameMatch = pattern.matcher(stockFrame);
				boolean b = stockFrameMatch.find();
				if (b) {
					frame = stockFrame.split("-")[0];
					serialNo = Long.parseLong(stockFrame.split("-")[1]);
				}
			}

			tYearOfManufacture = this.yearOfManufactureRepository
					.findOneByModelNoAndFrameAndSerialNoFromLessThanEqualAndSerialNoToGreaterThanEqual(modelNo, frame,
							serialNo, serialNo);
		} catch (Exception e) {
			return null;
		}
		return AppUtil.isObjectEmpty(tYearOfManufacture) ? null : tYearOfManufacture.getManufactureYear();
	}

	@Override
	public List<TYearOfManufacture> getListWithoutDelete() {
		MatchOperation match = Aggregation.match(Criteria.where("deleteFlag").is(Constants.DELETE_FLAG_0));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<TYearOfManufacture> result = this.mongoTemplate.aggregate(aggregation, "t_yr_mnfctr", TYearOfManufacture.class);
		return result.getMappedResults();
	}

	@Override
	public void editManufactureYear(String code, TYearOfManufacture tYearOfManufacture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TYearOfManufacture findOneByActiveCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
