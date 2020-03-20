package com.nexware.aajapan.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexware.aajapan.services.FBFeedService;

/**
 * Controller to serve facebook feed data request
 * 
 * @author Vignesh Velusamy(Nexware)
 * @since 17 Jul 2019
 *
 */
@RestController
public class FBFeedController {
	
	/**
	 * Service parameter to use for feed generation functionality
	 */
	@Autowired
	private FBFeedService fbFeedService;

	/**
	 * Method to serve the request to fetch the feed data.
	 * 
	 * @param response
	 */
	@GetMapping("/fbfeeds")
	public void getFeedCSV(HttpServletResponse response) {

	fbFeedService.loadFeedDataFromStock(response);
	}

}