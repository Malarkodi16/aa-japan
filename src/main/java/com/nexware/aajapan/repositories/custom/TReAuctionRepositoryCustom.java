package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.ProfitLossDto;
import com.nexware.aajapan.dto.TReAuctionDto;

public interface TReAuctionRepositoryCustom {

	List<TReAuctionDto> getTReAuctionInitiatedList();

	TReAuctionDto getTReAuctionInitiatedListById(String id);

	Long getTReAuctionInitiatedListCount();
	
	List<ProfitLossDto> getProfitLosslist();

}
