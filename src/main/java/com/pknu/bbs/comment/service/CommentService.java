package com.pknu.bbs.comment.service;

import java.util.List;

import com.pknu.bbs.comment.dto.CommentDto;

public interface CommentService {
//	void read(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
//	void write(CommentDto comment);
	
	void insertComment(CommentDto comment);

	List<CommentDto> getComments(int articleNum, int commentRow);
}
