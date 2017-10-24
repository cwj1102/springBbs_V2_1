package com.pknu.bbs.bbs.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.ui.Model;

import com.pknu.bbs.bbs.dto.BBSDto;

public interface BBSService {
	public void list(int pageNum, Model model);
	void content(int fileStatus, String articleNum, Model model);
	void delete(String articleNum, int fileStatus) throws ServletException, IOException;
//	void update(Model model, String articleNum, String title, String content) throws ServletException, IOException;
	void updateForm(String articleNum, int fileStatus, Model model) throws ServletException, IOException;
	public void download(String storedFname, HttpServletResponse resp);
	public FileSystemResource download(HttpServletResponse resp, String storedFname);
	public void update(BBSDto article, String[] deleteFileNames, Model model, int fileCount);
}
