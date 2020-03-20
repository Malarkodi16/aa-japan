package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MSupplier;

public interface MasterSupplierRepositoryCustom {
	List<MSupplier> getSupplierListBySearchTerm(String search, String criteria);

	MSupplier getSupplierByName(String supplier);

	List<String> getPOSNos(String supplier, String auctionHouse);

	List<MSupplier> getListWithoutDeletedSuppliers();

	MSupplier findOneActiveSupplier(String supplierCode);

	boolean isExistsByCompany(String company, Integer deleteFlag);

	boolean isExistsBySupplierCodeAndCompany(String supplierCode, String company, Integer deleteFlag);

	void updateBySupplierCode(String supplierCode, Integer deleteFlag);

}
