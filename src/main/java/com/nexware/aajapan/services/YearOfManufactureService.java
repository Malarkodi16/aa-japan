package com.nexware.aajapan.services;

import java.util.Date;
import java.util.List;

import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.TYearOfManufacture;

public interface YearOfManufactureService {

	void saveManufactureYear(TYearOfManufacture tYearOfManufacture);

	void editManufactureYear(String code, TYearOfManufacture tYearOfManufacture);
	
	Date yearOfManufacture(String stockFrame, String stockModelNo);
	List<TYearOfManufacture> getListWithoutDelete();
	TYearOfManufacture findOneByActiveCode(String code);
}
