package com.nexware.aajapan.services.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.nexware.aajapan.models.FBFeedDto;
import com.nexware.aajapan.repositories.StockRepository;
import com.nexware.aajapan.services.FBFeedService;

import constants.FbFeedConstants;

/**
 * @author Vignesh Velusamy(Nexware)
 * @since 17 Jul 2019
 *
 */
@Service
@Transactional
public class FBFeedServiceImpl implements FBFeedService {

	/**
	 * Injected Stock
	 */
	@Autowired
	private StockRepository stockRepository;

	/**
	 * Method to generate the FB feed data file by getting information from the
	 * Stock table.
	 */
	@Override
	public void loadFeedDataFromStock(HttpServletResponse response) {
		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", FbFeedConstants.FILENAME);
		response.setHeader(headerKey, headerValue);
		String[] header = FbFeedConstants.HEADER;

		List<FBFeedDto> feedData = stockRepository.findStockForFbFeed();

		try (
				// uses the Super CSV API to generate CSV data from the model data
				ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
						CsvPreference.STANDARD_PREFERENCE);) {
			csvWriter.writeHeader(header);
			for (FBFeedDto aBook : feedData) {

				csvWriter.write(aBook, header);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
