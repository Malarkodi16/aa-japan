package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.MShipDto;
import com.nexware.aajapan.models.MAuctionGradesExterior;
import com.nexware.aajapan.models.MShip;

public interface MasterShipRepositoryCustom {

	List<MShipDto> getShipsWithCompanyName();

	List<MShip> getAllUnDeletedShip();

	boolean existsByShipIdAndName(String shipId, String name);

	boolean existsByName(String name);
}
