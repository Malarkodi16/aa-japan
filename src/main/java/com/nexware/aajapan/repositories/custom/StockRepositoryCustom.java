package com.nexware.aajapan.repositories.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.nexware.aajapan.datatable.DataTableRequest;
import com.nexware.aajapan.dto.BLDto;
import com.nexware.aajapan.dto.BranchSalesOrderDto;
import com.nexware.aajapan.dto.GLDto;
import com.nexware.aajapan.dto.InspectionDto;
import com.nexware.aajapan.dto.InventoryValueDto;
import com.nexware.aajapan.dto.PurchasedDto;
import com.nexware.aajapan.dto.SalesStockSearchDto;
import com.nexware.aajapan.dto.ShippingAvailableStockDto;
import com.nexware.aajapan.dto.ShippingStockSearchDto;
import com.nexware.aajapan.dto.SpecialUserDto;
import com.nexware.aajapan.dto.SpecialUserStockSearchDto;
import com.nexware.aajapan.dto.StockDetailsDto;
import com.nexware.aajapan.dto.StockDocumentsDto;
import com.nexware.aajapan.dto.StockFilter;
import com.nexware.aajapan.dto.StockReserveTableShowDto;
import com.nexware.aajapan.dto.StockSearchDto;
import com.nexware.aajapan.dto.TLastLapVehiclesDto;
import com.nexware.aajapan.dto.TStockDto;
import com.nexware.aajapan.dto.TStockMakerModelDto;
import com.nexware.aajapan.dto.TStockShippingListDto;
import com.nexware.aajapan.dto.TransportRearrangeDto;
import com.nexware.aajapan.models.FBFeedDto;
import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.models.TransportInfo;

public interface StockRepositoryCustom {
	List<PurchasedDto> getAllPurchasedData(String invoiceItemType);

	List<StockDocumentsDto> findAllDocumentNotReceivedList();

	StockDocumentsDto findOneDocumentNotReceivedStockDetails(String stockNo);

	List<StockDocumentsDto> findAllDocumentNameTransferList();

	List<StockDocumentsDto> findAllDocumentDomesticList();

	List<TStockDto> getAllPurchaseconfirmedData();

	List<TStockDto> getAllMonthWisePurchaseconfirmedData(String period, Date from, Date to);

	List<InspectionDto> findAllByInspectionStatus();

	List<InspectionDto> findAllByInspectionStatusAndStockNos(List<String> stockNos);

	void updateTransportStatusByStockNos(int statusList, List<String> stockNos);

	void updateTransportStatusByStockNo(int status, String stockNo);

	void updateTransportCancelInfo(String reasonForCancel, int status, String stockNo);

	void updateTransportCancelInfo(String reasonForCancel, int status, List<String> stockNo);

	void updateStatusByStockNos(int statusList, List<String> stockNos);

	UpdateResult updateByStockNos(List<String> stockNos, Update update);

	UpdateResult updateByStockNo(String stockNo, Update update);

	void updateStatusByStockNo(Integer statusList, String stockNo);

	void updateTransportInfo(String stockNo, TransportInfo transportInfo);

	void lockStocks(List<String> stockNos, String lockedBy, String lockedBySalesPersonName);

	void unLockStocks(List<String> stockNos, String lockedBy, String lockedBySalesPersonName);

	ShippingStockSearchDto getStockShippingList(DataTableRequest<TStockShippingListDto> dataTableInRQ,
			StockFilter filter);

	SalesStockSearchDto getSalesStockSearchList(StockFilter filter);

	SpecialUserStockSearchDto getSpecialUserStockList(StockFilter filter);

	List<TransportRearrangeDto> findAllRearrangeItems();

	List<TStock> findBySearchTerms(String search);

	List<BLDto> getBLList();

	List<GLDto> getExecuteGLList();

	void updateInspectionStatusByStockNo(int status, List<String> stockNos);

	void stockUnreserve(List<String> status);

	List<StockReserveTableShowDto> findByReserveStock(List<String> salesPersonIds, Integer isBidding);

	List<TLastLapVehiclesDto> getAllLastLapVehiclesList();

	List<ShippingAvailableStockDto> findAllAvailableStocksForShipping();

	Long findByReserveStockCount(List<String> salesPersonIds);

	Long findAllByDocumentStatusCount();

	Long findAllByDocumentCertificateStatusCount();

	Long findAllByDocumentNameTransferStatusCount();

	Long findAllByDocumentDomesticStatusCount();

	List<TStock> findBySearch(String search);

	List<StockSearchDto> findBySearchDto(String search);

	List<StockSearchDto> findShippingAvailableStock(String search);

	List<StockSearchDto> findCancelledStock(String search, Date purchaseDate);

	List<StockSearchDto> findReauctionStock(String search, Date purchaseDate);

	List<StockSearchDto> searchRikusoStock(String search);

	List<StockSearchDto> searchTakeOutStock(String search, String auctionCompany, String auctionHouse);

	void updateInspection(List<String> equipment, String stockNo, String engineNo, String chassisNo, String color);

	boolean isExistsByChassisNo(String chassisNo);

	boolean isExistsByStockNoAndChassisNo(String stockNo, String chassisNo);

	StockDetailsDto findOneStockDetailsByStockNo(String stockNo);

	List<StockDocumentsDto> findAllExportTrackingList();

	List<InventoryValueDto> getInventoryValueReport();

	List<BranchSalesOrderDto> getShippedStockWithoutSalesOrder();

	Long countByStatusPurchaseConfirmedAndSold();

	List<FBFeedDto> findStockForFbFeed();

	Integer getYearlySalesTotal();

	Map<String, Integer> getMonthWiseSalesData();

	Map<String, Integer> getTopCustomerData();

	Map<String, Integer> getTopModelData();

	SpecialUserDto findOneSpecialUserByStockNo(String stockNo);

	void updateSalesPersonByStockNos(String salesPersonId, List<String> stockNos);

	void updateStock(Update update, String id);
}
