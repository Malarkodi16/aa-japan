package com.nexware.aajapan.services;

import javax.servlet.http.HttpServletResponse;

public interface ShippingRequestService {
	void exportContainerRequestExcel(String allocationId, HttpServletResponse response);
}
