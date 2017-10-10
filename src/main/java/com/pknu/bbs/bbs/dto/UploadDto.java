package com.pknu.bbs.bbs.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadDto {
	private int fileNum;
	private String originFname;
	private String storedFname;
	private long fileLength;
	private int articleNum;
	
	private CommonsMultipartFile fileData;
	
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	public String getOriginFname() {
		return originFname;
	}
	public void setOriginFname(String originFname) {
		this.originFname = originFname;
	}
	public String getStoredFname() {
		return storedFname;
	}
	public void setStoredFname(String storedFname) {
		this.storedFname = storedFname;
	}
	public CommonsMultipartFile getFileData() {
		return fileData;
	}
	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public int getArticleNum() {
		return articleNum;
	}
	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
	@Override
	public String toString() {
		return "UploadDto [fileNum=" + fileNum + ", originFname=" + originFname + ", storedFname=" + storedFname
				+ ", fileLength=" + fileLength + ", articleNum=" + articleNum + ", fileData=" + fileData + "]";
	}
	
}
