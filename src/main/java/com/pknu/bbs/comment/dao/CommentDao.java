package com.pknu.bbs.comment.dao;

import java.util.HashMap;
import java.util.List;

import com.pknu.bbs.comment.dto.CommentDto;

public interface CommentDao {

	void insertComment(CommentDto comment);
	public List<CommentDto> getComments(HashMap<String, Integer> commentMap);
	
	int commentsCount(int articleNum);
	/*	List<CommentDto> getComments(HashMap<Object,Object> paramMap) throws SQLException;
	void writeContent(HashMap<Object,Object> paramMap);*/
}
