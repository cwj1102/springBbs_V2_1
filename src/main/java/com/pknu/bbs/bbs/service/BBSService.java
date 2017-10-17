package com.pknu.bbs.bbs.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface BBSService {
	public void list(int pageNum, Model model);
	void content(int fileStatus, String articleNum, Model model);
	void delete(String articleNum) throws ServletException, IOException;
	void update(Model model, String articleNum, String title, String content) throws ServletException, IOException;
	void updateForm(String articleNum, Model model) throws ServletException, IOException;
	public void download(String storedFname, HttpServletResponse resp);
}
