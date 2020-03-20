package com.nexware.aajapan.services;

import javax.servlet.http.HttpServletResponse;

/**
 * Defines the functionalities that the FBFeed requires to generate data.
 * 
 * @author Vignesh Velusamy(Nexware)
 * @since 17 Jul 2019
 *
 */
public interface FBFeedService {
	
	/**
	 * Loads data from Stock table.
	 * 
	 * @param response
	 */
	public void loadFeedDataFromStock(HttpServletResponse response);

}
