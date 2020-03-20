package constants;

/**
 * @author Vignesh Velusamy (Nexware)
 * @since 12 Jul 2019
 * @version 1.0
 * 
 *          Class to file to maintain the default values to be used in the
 *          facebook feed CSV file to be generated on api call.
 */
public class FbFeedConstants {

	/**
	 * default latitude
	 */
	public static final String LATITUDE = "35.5428233";

	/**
	 * default longitude
	 */
	public static final String LONGITUDE = "139.600028";

	/**
	 * Default address value of the seller address
	 */
	public static final String ADDRESS = "{addr1: 'DS BLDG, 1-28-21 Hayabuchi, Tsuzuki-Ku,'" + ", city: 'Yokohama', "
			+ "region: 'Kanagawa', " + "postal_code: '224-0025', " + "country: 'JP'}";

	/**
	 * Hard coded value used to denote the mileage on the vehicle
	 */
	public static final String MILEAGE_UNIT_DEFAULT = "KM";

	/**
	 * Used value for the status of the vehicle
	 */
	public static final String USED = "Used";

	/**
	 * New value for the status of the vehicle
	 */
	public static final String NEW = "New";

	/**
	 * Available value for the status of the vehicle
	 */
	public static final String AVAILABLE = "Available";

	/**
	 * Time format to be used by the formatter to extract Year
	 */
	public static final String TIMEFORMAT = "EEE MMM dd HH:mm:ss z yyyy";

	/**
	 * URL to be used in the descriprtion
	 */
	public static final String URL = "www.aajapancars.com/Stock/";

	/**
	 * URL to be appended in the items in the Image Array
	 */
	public static final String IMAGE_URL = "https://images.aajapancars.com/";

	/**
	 * List of Header values in the CSV file
	 */
	public static final String[] HEADER = { "vehicle_id", "title", "description", "url", "make", "model", "year",
			"mileage_value", "mileage_unit", "image_url", "image_tag", "transmission", "fuel_type", "body_style",
			"drivetrain", "vin", "condition", "price", "address", "exterior_color", "sale_price", "availability",
			"state_vehicle", "latitude", "longitude" };

	/**
	 * File name for the generated CSV
	 */
	public static final Object FILENAME = "FBFeed.csv";

	/**
	 * Web API url to get the CRM Stock number for the corresponding stock number
	 * from ERP
	 */
	public static final String STOCK_API_URL = "http://stockapi.aajapancars.com/Api/WebApi/getStockNoFromChassisNo?chassisNo=";
	
	/**
	 *  Access key for the AWS Credentials
	 */
	public static final String AWSCREDENTIAL_ACCESS_KEY = "AKIAJ5VTUXB5VHNG7G7A";
	
	/**
	 *  Secret key for the AWS Credentials
	 */
	public static final String AWSCREDENTIAL_SECRET_KEY = "/l5J8ocPetnT8aVWqsEwPQFlX5EtV4vpCHCwkxz/";
	
	/**
	 *  S3 bucket name of AWS image stored
	 */
	public static final String AWSTOCK_IMAGE_BUCKET_NAME = "aajapanstock";

}
