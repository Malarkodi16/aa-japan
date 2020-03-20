package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.models.MShippingTerms;

public interface MShippingTermsRepositoryCustom {
	List<MShippingTerms> getAllUnDeletedShippingTerms();
}
