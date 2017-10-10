package com.pknu.bbs.bbs.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.transaction.annotation.Transactional;

import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
@Transactional
public interface BBSDao {

	int getTotalCount();

	List<BBSDto> getArticleList(HashMap<Object, Object> paramMap) throws SQLException;
	
	BBSDto getContent(String articleNum) throws SQLException;
	@Transactional
	void write(BBSDto article) throws ServletException, IOException;
	
	void join(HashMap<Object,Object> paramMap);
	
	
	
	void delete(String articleNum) throws SQLException;
	
	BBSDto getUpdateArticle(String articleNum) throws SQLException;
	
	void updateArticle(HashMap<Object,Object> paramMap) throws SQLException;
	
	String loginCheck(String id) throws SQLException;


	String joinCheck(String id);

	void posUpdate(HashMap<Object, Object> paramMap);

	void reply(BBSDto article);
	
	int commentsCount(int articleNum);

	void writeUpload(HashMap hm);

	List<UploadDto> getFileStatus(String articleNum);

	UploadDto getDownloadStatus(String fname);
	@Transactional
	void fileUpload(HashMap<String, Object> hm);

}
