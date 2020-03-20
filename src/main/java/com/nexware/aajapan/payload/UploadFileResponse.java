package com.nexware.aajapan.payload;

public class UploadFileResponse {
	private String fileName;
	private String diskFileName;
	private String directory;
	private String fileDownloadUri;
	private String fileType;
	private long size;

	public UploadFileResponse(String fileName, String diskFileName, String directory, String fileDownloadUri,
			String fileType, long size) {
		this.fileName = fileName;
		this.diskFileName = diskFileName;
		this.directory = directory;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return this.fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDirectory() {
		return this.directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDiskFileName() {
		return this.diskFileName;
	}

	public void setDiskFileName(String diskFileName) {
		this.diskFileName = diskFileName;
	}

}
