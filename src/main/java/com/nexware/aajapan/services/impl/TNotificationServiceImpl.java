package com.nexware.aajapan.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexware.aajapan.core.Constants;
import com.nexware.aajapan.dto.MLoginDto;
import com.nexware.aajapan.dto.PurchasedSaveDto;
import com.nexware.aajapan.dto.TNotificationDto;
import com.nexware.aajapan.dto.TShippingRequestedCreateDto;
import com.nexware.aajapan.models.TCustomer;
import com.nexware.aajapan.models.TNotification;
import com.nexware.aajapan.models.TShippingInstruction;
import com.nexware.aajapan.repositories.TNotificationRepository;
import com.nexware.aajapan.services.SecurityService;
import com.nexware.aajapan.services.TNotificationService;

@Service
@Transactional
public class TNotificationServiceImpl implements TNotificationService {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private TNotificationRepository notificationRepository;
	@Autowired
	private SecurityService securityService;

	@Override
	public void notify(List<TNotification> notification, String username) {
		List<String> ids = notification.stream().map(TNotification::getId).collect(Collectors.toList());
		List<String> toUserCodes = notification.stream().map(TNotification::getToUserCode).collect(Collectors.toList());
		List<TNotificationDto> notification1 = this.notificationRepository.getNotificationsByIds(ids);
		List<TNotificationDto> allNotificaton = this.notificationRepository.getListBasedOnLogin(toUserCodes);
		long count = allNotificaton.stream().filter(n -> n.getStatus() == Constants.TNOTIFICATION_CREATED).count();
		Map<String, Object> response = new HashMap<>();
		response.put("count", count);
		response.put("totalCount", allNotificaton.size());
		response.put("notifications", notification1);
		this.messagingTemplate.convertAndSendToUser(username, "/queue/notify", response);

	}

	@Override
	public List<TNotification> notifyDupliateCustomerToAdmin(TCustomer tCustomer) {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		TNotification notification = new TNotification();
		notification.setFromUserCode(loggedInUser.getUserId());
		notification.setMessage(Constants.TNOTIFICATION_CUSTOMER_DUPLICATION
				.replaceAll("<<mobileNo>>", tCustomer.getMobileNo()).replaceAll("<<city>>", tCustomer.getCity()));
		notification.setStatus(Constants.TNOTIFICATION_CREATED);
		notification.setToUserCode("USR1010");
		notification
				.setActionUrl("/customer/list?mobileNo=" + tCustomer.getMobileNo() + "&city=" + tCustomer.getCity());

		this.notificationRepository.save(notification);
		List<TNotification> notifications = new ArrayList<>();
		notifications.add(notification);
		return notifications;
	}

	@Override
	public List<TNotification> purchaseSaveNotificationToSales(List<PurchasedSaveDto> purchasedSaveDtos) {

		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		List<TNotification> notifications = new ArrayList<>();
		purchasedSaveDtos.forEach(notify -> {
			TNotification notification = new TNotification();
			notification.setFromUserCode(loggedInUser.getUserId());
			notification.setMessage(Constants.TNOTIFICATION_CONFIRM_PURCHASE_MESSAGE
					.replaceAll("<<maker>>", notify.getMaker()).replaceAll("<<model>>", notify.getModel()));
			notification.setStatus(Constants.TNOTIFICATION_CREATED);
			notification.setToUserCode("USR1008");
			notification.setActionUrl("#");
			notifications.add(notification);

		});
		this.notificationRepository.saveAll(notifications);
		return notifications;
	}

	@Override
	public List<TNotification> notifyAboutShippingInstruction(List<TShippingInstruction> shippingInstruction) {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		List<TNotification> notifications = new ArrayList<>();
		shippingInstruction.forEach(shippingInstructions -> {
			TNotification notification = new TNotification();
			notification.setFromUserCode(loggedInUser.getUserId());
			notification.setMessage(Constants.TNOTIFICATION_SHIPPING_INSTRUCTION_MESSAGE.replaceAll("<<destCountry>>",
					shippingInstructions.getDestCountry()));
			notification.setStatus(Constants.TNOTIFICATION_CREATED);
			notification.setToUserCode("USR1007");
			notification.setActionUrl("/shipping/arrangement?flag=notification&id=" + shippingInstructions.getId());
			notifications.add(notification);
		});

		this.notificationRepository.saveAll(notifications);
		return notifications;
	}

	@Override
	public List<TNotification> acceptShippingRequest(TShippingRequestedCreateDto shippingRequest) {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		List<TNotification> notifications = new ArrayList<>();
		shippingRequest.getStockShippingInstructionArray().forEach(notify -> {
			TNotification notification = new TNotification();
			notification.setFromUserCode(loggedInUser.getUserId());
			notification.setMessage(Constants.TNOTIFICATION_SHIPPING_ARRANGED.replaceAll("<<destCountry>>",
					shippingRequest.getDestCountry()));
			notification.setStatus(Constants.TNOTIFICATION_CREATED);
			notification.setToUserCode("USR1008");
			notification.setActionUrl("/sales/shippinginstruction?id=" + notify.getShippingInstructionId());
			notifications.add(notification);
		});
		this.notificationRepository.saveAll(notifications);
		return notifications;
	}

	@Override
	public List<TNotification> cancelSalesOrderNotification(String invoiceNo, String code) {
		MLoginDto loggedInUser = this.securityService.findLoggedInUser();
		List<TNotification> notifications = new ArrayList<>();
		System.out.println(invoiceNo);
		TNotification notification = new TNotification();
		notification.setFromUserCode(loggedInUser.getUserId());
		notification.setMessage(Constants.TNOTIFICATION_SALES_INVOICE_CANCEL.replaceAll("<<invoiceNo>>", invoiceNo));
		notification.setStatus(Constants.TNOTIFICATION_CREATED);
		notification.setToUserCode(code);
		notification.setActionUrl("#");
		notifications.add(notification);
		this.notificationRepository.saveAll(notifications);
		return notifications;
	}

}
