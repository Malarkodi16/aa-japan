package com.nexware.aajapan.models;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import constants.FbFeedConstants;

/**
 * 
 * This DTO is used to create the FB feed data from Stock information
 * 
 * @author Vignesh Velusamy(Nexware)
 * @since 07/July/2019
 */
@SuppressWarnings("unused")
public class FBFeedDto {

	/**
	 * ID of the vehicle
	 */
	private String vehicle_id;

	/**
	 * title for the feed entry
	 */
	private String title;

	/**
	 * url to see the vehicle listing
	 */
	private String url;

	/**
	 * make of the vehicle
	 */
	private String make;

	/**
	 * model of the vehicle
	 */
	private String model;

	/**
	 * year of manufacturing
	 */
	private String year;

	/**
	 * mileage on the vehicle
	 */
	private String mileage_value;

	/**
	 * Unit of mileage measure
	 */
	private String mileage_unit;

	/**
	 * URL for the Images of Vehicle
	 */
	private String image_url;

	/**
	 * A tag for th image of the vehicle. Example : Front,Side View, Interior
	 */
	private String image_tag;

	/**
	 * Type of the transmission of the vehicle
	 */
	private String transmission;

	/**
	 * Type of the Fuel Used
	 */
	private String fuel_type;

	/**
	 * Body Stylle of the Vehicle
	 */
	private String body_style;

	/**
	 * Drive train type
	 */
	private String drivetrain;

	/**
	 * VIN
	 */
	private String vin;

	/**
	 * Condition of the vehicle
	 */
	private String condition;

	/**
	 * Price of the vehicle
	 */
	private String price;

	/**
	 * Address of the selle
	 */
	private String address;

	/**
	 * Exterior color of the vehicle
	 */
	private String exterior_color;

	/**
	 * Sale Price
	 */
	private String sale_price;

	/**
	 * Availability status of the vehicle
	 */
	private String availability;

	/**
	 * Status of the vehicle Example : New,Used
	 */
	private String state_vehicle;

	/**
	 * Latitude of place where the vehicle is available
	 */
	private String latitude;

	/**
	 * Longitude of place where the vehicle is available
	 */
	private String longitude;

	/**
	 * Accessories available in the vehicle.
	 */
	private List<String> equipment;

	/**
	 * Model Type of the Vehicle
	 */
	private String modelType;

	/**
	 * Model Code of the vehicle
	 */
	private String modelCode;

	/**
	 * Additional accessories apart from the standard fittings
	 */
	private List<String> extraAccessories;

	/**
	 * Remarks
	 */
	private String remarks;

	/**
	 * AttachCount of the vehicle
	 */
	private String attachCount;

	/**
	 * Grade of the Vehicle
	 */
	private String grade;

	/**
	 * Description of the vehicle.
	 */
	private String description;

	/**
	 * Method to get the Stock ID from CRM for the corresponding ERP Stock ID value.
	 * 
	 * @return
	 */
	
     // note : you may also need
     //        HttpURLConnection.setInstanceFollowRedirects(false)
    
	public String getVehicle_id() {

		try {
			final String uri = FbFeedConstants.STOCK_API_URL + vin;
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			// Set the headers for the Api call
			headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			// Post the request and get the response and set the vehicle_id from the
			// response body.
			ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
			vehicle_id = (String) response.getBody();

		} catch (RestClientException restClientException) {
			restClientException.printStackTrace();

		}
		return vehicle_id;
	}

	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public String getTitle() {
		return getFormattedTitle();
	}

	/**
	 * Method to create the title String from make,model,year and vin in specified
	 * format.
	 * 
	 * @return title
	 */
	private String getFormattedTitle() {

		StringBuilder titleBuilder = new StringBuilder();

		titleBuilder.append(make).append(" ");
		titleBuilder.append(model).append(" ");
		titleBuilder.append(getYear()).append(" ");
		titleBuilder.append(vin);

		return titleBuilder.toString();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method to return description from modelType,grade,equipment,remarks
	 * 
	 * @return description
	 */
	public String getDescription() {
		StringBuilder descriptionBuilder = new StringBuilder();

		descriptionBuilder.append(modelType).append(" ");
		descriptionBuilder.append(grade).append(" ");
		if (!extraAccessories.isEmpty())
			descriptionBuilder.append(extraAccessories).append(" ");
		descriptionBuilder.append(getRemarks());

		return descriptionBuilder.toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return getFormattedUrlString();
	}

	/**
	 * Creates the url from in the specified format and returns.
	 * 
	 * @return url
	 */
	private String getFormattedUrlString() {

		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append(FbFeedConstants.URL);
		urlBuilder.append(getVehicle_id()).append("-");
		urlBuilder.append(getMake()).append("-");
		urlBuilder.append(getModel()).append("-");
		urlBuilder.append(getVin());

		return urlBuilder.toString();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Method to parse the timestamp and return the year value.
	 * 
	 * @return
	 */
	public String getYear() {

		// Parse the timestamp and return the year alone.
		return String.valueOf(
				java.time.LocalDate.parse(year, DateTimeFormatter.ofPattern(FbFeedConstants.TIMEFORMAT)).getYear()
				+" " +
				java.time.LocalDate.parse(year, DateTimeFormatter.ofPattern(FbFeedConstants.TIMEFORMAT)).getMonth());
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMileage_value() {
		return mileage_value;
	}

	public void setMileage_value(String mileage_value) {
		this.mileage_value = mileage_value;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public String getBody_style() {
		return body_style;
	}

	public void setBody_style(String body_style) {
		this.body_style = body_style;
	}

	/**
	 * Method to get the drivetrain type.
	 * 
	 * @return a string value of the Drivetrain type
	 */
	public String getDrivetrain() {
		if (null != equipment && !equipment.isEmpty()) {
			if (equipment.contains("4WD"))
				return "4X4";
		}
		return "4X2";
	}

	public void setDrivetrain(String drivetrain) {
		this.drivetrain = drivetrain;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAddress() {
		if (null == address) {
			address = FbFeedConstants.ADDRESS;
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getExterior_color() {
		return exterior_color;
	}

	public void setExterior_color(String exterior_color) {
		this.exterior_color = exterior_color;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getAvailability() {
		return FbFeedConstants.AVAILABLE;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	/**
	 * @return default value if the value is not set from the Database.
	 */
	public String getLatitude() {
		if (null == latitude) {
			latitude = FbFeedConstants.LATITUDE;
		}
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return default value if the value is not set from the Database. Uses the
	 *         longitude value defined in {@link FbFeedConstants}
	 */
	public String getLongitude() {
		if (null == longitude) {
			longitude = FbFeedConstants.LONGITUDE;
		}
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMileage_unit() {
		return FbFeedConstants.MILEAGE_UNIT_DEFAULT;
	}

	public void setMileage_unit(String mileage_unit) {
		this.mileage_unit = mileage_unit;
	}

	public String getImage_url() {
		StringBuilder urlBuilder = new StringBuilder();
		for (int i = 0; i < 20; i++) {

//			File temp = new File((FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg"));
//			URL url;
//			HttpURLConnection huc;
//			try {
//				url = new URL(FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg");
//				huc = (HttpURLConnection) url.openConnection();
//				int responseCode = huc.getResponseCode();
////				huc.setRequestMethod("HEAD");
//				if (responseCode == 400 ) {
//					System.out.println("GOOD");
//					urlBuilder.append("image[" + i + "]." + FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg");
//					urlBuilder.append(',');
//					} else {
//					System.out.println("BAD");
//					}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			  try {
				new URL(FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg").toURI();
				urlBuilder.append("image[" + i + "]." + FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg");
				urlBuilder.append(',');
				System.out.println("good");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("BAD");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("BAD");
			}
//			if (temp.getAbsoluteFile().exists()) {
//				urlBuilder.append("image[" + i + "]." + FbFeedConstants.IMAGE_URL + vin + "-" + (i + 1) + ".jpg");
//				urlBuilder.append(',');
//			}
		}

		return urlBuilder.toString();
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getImage_tag() {
		StringBuilder tagBuilder = new StringBuilder();
		for (int i = 0; i < Integer.parseInt(attachCount); i++) {
			tagBuilder.append("image[" + i + "]." + make + " " + model + " " + modelType);
			tagBuilder.append(',');
		}

		return tagBuilder.toString();
	}

	public void setImage_tag(String image_tag) {
		this.image_tag = image_tag;
	}

	public String getState_vehicle() {
		if (null != state_vehicle) {
			if (state_vehicle.equalsIgnoreCase("yes"))
				return FbFeedConstants.USED;
			else
				return FbFeedConstants.NEW;
		}
		return state_vehicle;
	}

	public void setState_vehicle(String state_vehicle) {
		this.state_vehicle = state_vehicle;
	}

	public List<String> getEquipment() {
		return equipment;
	}

	public void setEquipment(List<String> equipment) {
		this.equipment = equipment;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<String> getExtraAccessories() {
		return extraAccessories;
	}

	public void setExtraAccessories(List<String> extraAccessories) {
		this.extraAccessories = extraAccessories;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}
