package com.pknu.bbs.bbs.dto;

public class UploadDto {
	@Override
	public String toString() {
		return "UploadDto [fileNum=" + fileNum + ", storedFname=" + storedFname + ", fileLength=" + fileLength
				+ ", articleNum=" + articleNum + "]";
	}
	private int fileNum;
	private String storedFname;
	private long fileLength;
	private int articleNum;
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	public String getStoredFname() {
		return storedFname;
	}
	public void setStoredFname(String storedFname) {
		this.storedFname = storedFname;
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
	
}
