package com.nexware.aajapan.models;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nexware.aajapan.listeners.EntityModelBase;

@Document(collection = "m_lgn")
public class MLogin extends EntityModelBase {
	enum fields {
		id, userId
	}

	@Id
	private String id;
	@NotBlank
	@Size(max = 100)
	@Indexed(unique = true)
	private String username;
	@NotBlank
	private String password;
	private String department;
	private String role;
	private String reportTo;
	private String userId;
	private String location;
	private List<Integer> access;
	private String ip;
	private String fromTime;
	private String toTime;
	public MLogin() {

	}

	public MLogin(@NotBlank @Size(max = 100) String username, @NotBlank String password, String department, String role,
			String reportTo, String userId, String location) {
		super();
		this.username = username;
		this.password = password;
		this.department = department;
		this.role = role;
		this.reportTo = reportTo;
		this.userId = userId;
		this.location = location;
	}

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

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
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
