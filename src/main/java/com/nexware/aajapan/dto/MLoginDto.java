package com.nexware.aajapan.dto;

import java.sql.Date;
import java.util.List;

import com.nexware.aajapan.listeners.EntityModelBase;
import com.nexware.aajapan.models.MUser;

public class MLoginDto extends EntityModelBase {

	private String id;
	private String username;
	private String loginName;
	private String password;
	private String department;
	private String role;
	private String reportTo;
	private String userId;
	private MUser user;
	private Integer hierarchy;
	private String ip;
	private String fromTime;
	private String toTime;
	private List<Integer> access;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getReportTo() {
		return this.reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MUser getUser() {
		return this.user;
	}

	public void setUser(MUser user) {
		this.user = user;
	}

	public Integer getHierarchy() {
		return this.hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public List<Integer> getAccess() {
		return access;
	}

	public void setAccess(List<Integer> access) {
		this.access = access;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

}
