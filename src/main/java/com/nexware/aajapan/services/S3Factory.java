package com.nexware.aajapan.services;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import constants.FbFeedConstants;

@Service
public class S3Factory {

	final AWSCredentials credentials = new BasicAWSCredentials(FbFeedConstants.AWSCREDENTIAL_ACCESS_KEY,
			FbFeedConstants.AWSCREDENTIAL_SECRET_KEY);

	final AmazonS3 s3client = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTHEAST_1).build();

	public List<S3ObjectSummary> getNoOfAttachment(String chassisNo, String destCountry, String supplier) {

		String prefix = chassisNo.replaceAll("-", "\\\\-");
		if (destCountry.equalsIgnoreCase("KENYA")) {
			prefix = prefix + "\\-y\\d+.jpg";
		} else if (supplier.equalsIgnoreCase("USS")) {
			prefix = prefix + "\\-u\\d+.jpg";
		} else if (supplier.equalsIgnoreCase("AUCNET")) {
			prefix = prefix + "\\-i\\d+.jpg";
		} else {
			prefix = prefix + "\\-\\d+.jpg";
		}

		ObjectListing listing = s3client.listObjects(FbFeedConstants.AWSTOCK_IMAGE_BUCKET_NAME, chassisNo);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();

		while (listing.isTruncated()) {
			listing = s3client.listNextBatchOfObjects(listing);
			summaries.addAll(listing.getObjectSummaries());
		}
		final Pattern pattern = Pattern.compile("(?sim)" + prefix, Pattern.MULTILINE);

		List<S3ObjectSummary> summary = summaries.stream().filter(a -> pattern.matcher(a.getKey()).find())
				.collect(Collectors.toList());
		return summary;
	}

}
