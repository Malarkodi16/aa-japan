package com.nexware.aajapan.repositories.custom;

import java.util.List;

import com.nexware.aajapan.dto.TNotificationDto;

public interface TNotificationRepositoryCustom {

	List<TNotificationDto> getListOnLoad();

	List<TNotificationDto> getNotificationsByIds(List<String> ids);

	List<TNotificationDto> getListBasedOnLogin(List<String> toUserCodes);

	TNotificationDto getById(String id);

}
