package com.nexware.aajapan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "t_ntfcn")
public class TNotification extends EntityModelBase {

	@Id
	private String id;
	private String message;
	@Indexed
	private String fromUserCode;
	@Indexed
	private String toUserCode;
	private Integer status;
	private String actionUrl;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromUserCode() {
		return this.fromUserCode;
	}

	public void setFromUserCode(String fromUserCode) {
		this.fromUserCode = fromUserCode;
	}

	public String getToUserCode() {
		return this.toUserCode;
	}

	public void setToUserCode(String toUserCode) {
		this.toUserCode = toUserCode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getActionUrl() {
		return this.actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

}
