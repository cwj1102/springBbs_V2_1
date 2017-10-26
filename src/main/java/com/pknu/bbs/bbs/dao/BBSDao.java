package com.pknu.bbs.bbs.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
@Transactional
public interface BBSDao {

	int getTotalCount();

	List<BBSDto> getArticleList(HashMap<Object, Object> paramMap) throws SQLException;
	
	BBSDto getContent(String articleNum) throws SQLException;
	@Transactional
	void write(BBSDto article);
	
	void join(HashMap<Object,Object> paramMap);
	
	
	
	void delete(String articleNum) throws SQLException;
	
	BBSDto getUpdateArticle(String articleNum);
	
	void updateArticle(BBSDto article);
	
	String loginCheck(String id) throws SQLException;


	String joinCheck(String id);

//	void posUpdate(HashMap<Object, Object> paramMap);

	void reply(BBSDto article);
	
	int commentsCount(int articleNum);

//	void writeUpload(HashMap hm);

	List<String> getFileStatus(String articleNum);

	UploadDto getDownloadStatus(String fname);
	/*@Transactional
	void fileUpload(HashMap<String, Object> hm);*/

	int getNextArticleNum();

	void insertFile(UploadDto uploadDto);
	
	public List<String> getStoredFnames(int articleNum);
//	void write1(BBSDto article);

	void dbDelFileName(ArrayList<String> delFileList);
	void dbDelFileName1(String[] delFileList);

}
