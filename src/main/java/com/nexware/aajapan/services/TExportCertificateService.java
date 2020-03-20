package com.nexware.aajapan.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import com.nexware.aajapan.models.TExportCertificate;

public interface TExportCertificateService {

	void saveExportCertificate(TExportCertificate exportCertificate);

	void updateDocumentFobPrice(Map<String, Object> data) throws IOException, ParseException;

}
