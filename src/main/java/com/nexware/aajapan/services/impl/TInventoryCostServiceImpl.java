package com.nexware.aajapan.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.models.TInventoryCost;
import com.nexware.aajapan.repositories.TInventoryCostRepository;
import com.nexware.aajapan.services.TInventoryCostService;

@Service
@Transactional
public class TInventoryCostServiceImpl implements TInventoryCostService {
	@Autowired
	private TInventoryCostRepository inventoryCostRepository;

	@Override
	public void saveInventoryCost(List<TInventoryCost> inventoryCosts) {
		this.inventoryCostRepository.saveAll(inventoryCosts);
	}

}
