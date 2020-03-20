package com.nexware.aajapan.repositories;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.nexware.aajapan.models.TStock;
import com.nexware.aajapan.repositories.custom.StockRepositoryCustom;

@Repository
@JaversSpringDataAuditable
public interface StockRepository extends MongoRepository<TStock, String>, StockRepositoryCustom {

	TStock findOneById(String id);

	TStock findOneByStockNo(String stockNo);

	void deleteByStockNo(String stockNo);

	List<TStock> findAllByStockNoIn(List<String> stockNos);

	List<TStock> findAllByStatus(Integer status);

	List<TStock> findAllByReserve(Integer reserve);

	boolean existsByStockNo(String stockNo);

	Long countByStatus(Integer status);

	List<TStock> findAllByDocumentStatus(Integer documentStatus);

	List<TStock> findAllByDocumentConvertTo(Integer documentConvertTo);

	@Query("{$and:[{'chassisNo':?0}, {'purchaseInfo.date' : { $gte: ?1, $lte: ?2 }} ] }")
	TStock findByChassisNoAndPurchaseInfoDate(String chassisNo, Date startDate, Date endDate);

	@Query("{$and:[{'purchaseInfo.auctionInfo.lotNo':?0},{'purchaseInfo.date' : { $gte: ?1, $lte: ?2 }} ,{'purchaseInfo.supplier':?3},{'purchaseInfo.auctionInfo.auctionHouse':?4}] }")
	TStock findByLotNoAndPurchaseInfoDate(Long lotNo, Date startDate, Date endDate, String supplier,
			ObjectId auctionHouse);

	@Query("{$and:[{'stockNo':?0},{'purchaseInfo.auctionInfo.lotNo':?1},{'purchaseInfo.date' : { $gte: ?2, $lte: ?3 }} ,{'purchaseInfo.supplier':?4},{'purchaseInfo.auctionInfo.auctionHouse':?5}] }")
	TStock findByStockNoLotNoAndPurchaseInfoDate(String stockNo, Long lotNo, Date startDate, Date endDate,
			String supplier, ObjectId auctionHouse);

	@Query("{$and:[ {'chassisNo':?0} , {'status': { $ne: ?1 }} ] }")
	TStock findByChassisNoAndStatus(String chassisNo, Integer status);

	TStock findOneByChassisNo(String chassisNo);
}
