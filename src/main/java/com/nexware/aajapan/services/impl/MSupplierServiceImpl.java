package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.exceptions.AAJRuntimeException;
import com.nexware.aajapan.models.MLocation;
import com.nexware.aajapan.models.MSupplier;
import com.nexware.aajapan.models.SupplierLocation;
import com.nexware.aajapan.models.TSupplierTransaction;
import com.nexware.aajapan.repositories.LocationRepository;
import com.nexware.aajapan.repositories.MasterSupplierRepository;
import com.nexware.aajapan.repositories.TSupplierTransactionRepository;
import com.nexware.aajapan.services.MSupplierService;
import com.nexware.aajapan.services.SequenceService;
import com.nexware.aajapan.utils.AppUtil;

@Service
@Transactional
public class MSupplierServiceImpl implements MSupplierService {

	@Autowired
	private TSupplierTransactionRepository supplierTransactionRepository;
	@Autowired
	private MasterSupplierRepository supplierRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void supplierTransactionEntry(TSupplierTransaction transaction) {
		Double closingBalance = this.modifyAndGetCurrentBalance(transaction.getSupplierCode(), transaction.getAmount(),
				transaction.getTransactionType());
		Double balance = null;
		if (transaction.getTransactionType() == Constants.TRANSACTION_CREDIT) {
			balance = closingBalance + transaction.getAmount();

		} else if (transaction.getTransactionType() == Constants.TRANSACTION_DEBIT) {
			balance = closingBalance - transaction.getAmount();
		}
		transaction.setBalance(balance);
		transaction.setClosingBalance(closingBalance);
		this.supplierTransactionRepository.insert(transaction);

	}

	@Override
	public Double modifyAndGetCurrentBalance(String supplierCode, Double amount, int transactionType) {
		Query query = new Query(Criteria.where("supplierCode").is(supplierCode));
		// update balance
		Update update = new Update();
		if (transactionType == Constants.TRANSACTION_CREDIT) {
			update.inc("balance", amount);
		} else if (transactionType == Constants.TRANSACTION_DEBIT) {
			update.inc("balance", (amount * -1));
		}
		// return new balance
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(false);
		MSupplier updated = this.mongoTemplate.findAndModify(query, update, options, MSupplier.class);

		if (AppUtil.isObjectEmpty(updated)) {
			throw new AAJRuntimeException("Exception while update supplier balance : " + supplierCode);
		}
		return updated.getBalance();
	}

	@Override
	public void supplierTransactionEntries(List<TSupplierTransaction> supplierTransactions) {
		supplierTransactions.forEach(transaction -> this.supplierTransactionEntry(transaction));

	}

	@Override
	public void createSupplier(MSupplier supplier) {
		supplier.setSupplierCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_SUPPLIER));
		supplier.setBalance(0.0);
		supplier.setDeleteStatus(Constants.MLOCATION_DELETE_STATUS_INITIALLY);
		supplier.getSupplierLocations().forEach(location -> {
			location.setId(ObjectId.get().toString());
			location.setDeleteStatus(Constants.MLOCATION_SUPPLIER_DELETE_STATUS_INITIALLY);
		});
		MSupplier savedSupplier = this.supplierRepository.save(supplier);
		// create entry in location table
		List<MLocation> mLocationList = new ArrayList<>();
		savedSupplier.getSupplierLocations().forEach(supplierLocation -> {
			MLocation mLocation = new MLocation();
			mLocation.setCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MLOC));
			mLocation.setType(supplier.getType());
			mLocation.setDeleteFlag(Constants.DELETE_FLAG_0);
			String displayName = "";
			if (supplier.getType().equalsIgnoreCase("auction")) {
				displayName = AppUtil.isObjectEmpty(supplierLocation.getAuctionHouse()) ? ""
						: "[" + supplierLocation.getAuctionHouse() + "]";
				displayName = supplier.getCompany() + displayName;
			} else {
				displayName = supplier.getCompany();
			}
			mLocation.setDisplayName(displayName);
			mLocation.setAddress(supplierLocation.getAddress());
			mLocation.setPhone(supplierLocation.getPhone());
			mLocation.setFax(supplierLocation.getFax());
			mLocation.setSupplierCode(savedSupplier.getSupplierCode());
			mLocation.setAuctionHouseId(supplierLocation.getId());
			mLocationList.add(mLocation);

		});
		this.locationRepository.saveAll(mLocationList);
	}

	@Override
	public void editSupplier(String supplierCode, MSupplier supplier) {
		// update
		MSupplier updateSupplier = this.supplierRepository.findOneBySupplierCode(supplierCode);
		updateSupplier.setSupplierCode(supplierCode);
		updateSupplier.setType(supplier.getType());
		updateSupplier.setCompany(supplier.getCompany());
		updateSupplier.setMaxDueDays(supplier.getMaxDueDays());
		updateSupplier.setMaxCreditAmount(supplier.getMaxCreditAmount());
		updateSupplier.setDeleteStatus(Constants.MLOCATION_DELETE_STATUS_INITIALLY);
		List<SupplierLocation> supplierLocations = updateSupplier.getSupplierLocations();
		List<SupplierLocation> newSupplierLocations = supplier.getSupplierLocations();

		List<String> newSupplierLocationsIds = newSupplierLocations.stream().map(SupplierLocation::getId)
				.collect(Collectors.toList());
		List<String> existingLocationIds = supplierLocations.stream().map(SupplierLocation::getId)
				.collect(Collectors.toList());

		List<String> toDeleteIds = supplierLocations.stream()
				.filter(location -> existingLocationIds.contains(location.getId())
						&& !newSupplierLocationsIds.contains(location.getId()))
				.map(SupplierLocation::getId).collect(Collectors.toList());
		// for edit and delete
		for (SupplierLocation supplierLocation : newSupplierLocations) {
			SupplierLocation location = null;
			int flag = -1;
			if (AppUtil.isObjectEmpty(supplierLocation.getId())) {
				location = new SupplierLocation();
				location.setId(ObjectId.get().toString());
				location.setDeleteStatus(Constants.MLOCATION_SUPPLIER_DELETE_STATUS_INITIALLY);

				MLocation mLocation = new MLocation();
				mLocation.setCode(this.sequenceService.getNextSequence(Constants.SEQUENCE_KEY_MLOC));
				mLocation.setType(supplier.getType());
				mLocation.setDeleteFlag(Constants.DELETE_FLAG_0);
				String displayName = "";
				if (supplier.getType().equalsIgnoreCase("auction")) {
					displayName = AppUtil.isObjectEmpty(supplierLocation.getAuctionHouse()) ? ""
							: "[" + supplierLocation.getAuctionHouse() + "]";
					displayName = new StringBuilder(supplier.getCompany()).append(displayName).toString();
				} else {
					displayName = supplier.getCompany();
				}
				mLocation.setDisplayName(displayName);
				mLocation.setAddress(supplierLocation.getAddress());
				mLocation.setPhone(supplierLocation.getPhone());
				mLocation.setFax(supplierLocation.getFax());
				mLocation.setSupplierCode(updateSupplier.getSupplierCode());
				mLocation.setAuctionHouseId(location.getId());
				this.locationRepository.save(mLocation);
				flag = 0;// new
			} else {
				Optional<SupplierLocation> existingLocation = null;
				existingLocation = supplierLocations.stream()
						.filter(loc -> loc.getId().equals(supplierLocation.getId())).findFirst();
				if (existingLocation.isPresent()) {
					location = existingLocation.get();
					location.setDeleteStatus(supplierLocation.getDeleteStatus());
					flag = 1;// update
				}
			}
			if (location != null) {
				location.setCode(supplierLocation.getCode());
				location.setAuctionHouse(supplierLocation.getAuctionHouse());
				location.setAddress(supplierLocation.getAddress());
				location.setPhone(supplierLocation.getPhone());
				location.setEmail(supplierLocation.getEmail());
				location.setFax(supplierLocation.getFax());
				location.setPosNos(supplierLocation.getPosNos());

				if (flag == 0) {
					supplierLocations.add(location);
				}
			}

		}
		supplierLocations.stream().filter(location -> toDeleteIds.contains(location.getId()))
				.forEach(location -> location.setDeleteStatus(Constants.DELETE_FLAG_1));

		// update location
		if (!AppUtil.isObjectEmpty(toDeleteIds) && !toDeleteIds.isEmpty()) {
			List<MLocation> locations = this.locationRepository.findAllByAuctionHouseIdIn(
					toDeleteIds.stream().map(id -> new ObjectId(id)).collect(Collectors.toList()));
			locations.forEach(loc -> loc.setDeleteFlag(Constants.DELETE_FLAG_1));
			this.locationRepository.saveAll(locations);
		}
		// save supplier
		this.supplierRepository.save(updateSupplier);

	}
}
