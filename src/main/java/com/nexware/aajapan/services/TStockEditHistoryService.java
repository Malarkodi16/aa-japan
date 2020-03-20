package com.nexware.aajapan.services;

import com.nexware.aajapan.models.TStock;

public interface TStockEditHistoryService {

	void compareStockObject(TStock oldObject, TStock newObject) throws IllegalArgumentException, IllegalAccessException;

}
