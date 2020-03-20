package com.nexware.aajapan.models;

import org.bson.types.ObjectId;

public class Attachment {

	private ObjectId id;
	private String stockNo;
	private String subDirectory;
	private String filename;
	private String diskFilename;
	private String fileSize;
	private String description;
	private String diskDirectory;

	public Attachment() {
		this.id = ObjectId.get();
	}

	public ObjectId getId() {
		return this.id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStockNo() {
		return this.stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getSubDirectory() {
		return this.subDirectory;
	}

	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDiskFilename() {
		return this.diskFilename;
	}

	public void setDiskFilename(String diskFilename) {
		this.diskFilename = diskFilename;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiskDirectory() {
		return this.diskDirectory;
	}

	public void setDiskDirectory(String diskDirectory) {
		this.diskDirectory = diskDirectory;
	}

}
