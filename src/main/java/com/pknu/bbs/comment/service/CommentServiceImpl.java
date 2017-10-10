package com.pknu.bbs.comment.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pknu.bbs.comment.dao.CommentDao;
import com.pknu.bbs.comment.dto.CommentDto;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private CommentDao commentDao;
	
	@Override
	public void insertComment(CommentDto comment) {
		commentDao.insertComment(comment);
	}

	@Override
	public List<CommentDto> getComments(int articleNum, int commentRow) {
		HashMap<String,Integer> commentMap = new HashMap<>();
		commentMap.put("articleNum", articleNum);
		commentMap.put("commentRow", commentRow);
		return commentDao.getComments(commentMap);
	}



/*	public void read(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String articleNum = req.getParameter("articleNum");
	String commentRow = req.getParameter("commentRow");
	List<CommentDto> commentList = null;
	try {
		HashMap<Object, Object> paramMap = new HashMap<>();
		paramMap.put("articleNum", articleNum);
		paramMap.put("commentRow", commentRow);

//		commentList = bbsdao.getComments(paramMap);
	} catch (Exception e) {
		e.printStackTrace();
	}
	resp.setCharacterEncoding("utf-8");
	JSONArray jb = new JSONArray(commentList);
	PrintWriter pw = resp.getWriter();
	pw.println(jb.toString());
	
	}
	
	public void write(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String content = req.getParameter("commentContent");
		String articleNum = req.getParameter("articleNum");
		String id = (String) req.getSession().getAttribute("id");
		System.out.println("라이트임플");
		
		HashMap<Object,Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("content", content);
		paramMap.put("articleNum", articleNum);
		
//		bbsdao.writeContent(paramMap);
		List<CommentDto> commentList = null;
//		try {
//
//			paramMap = new HashMap<>();
//			paramMap.put("articleNum", articleNum);
//			paramMap.put("commentRow", "10");
//
////			commentList = bbsdao.getComments(paramMap);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		resp.setCharacterEncoding("utf-8");
		JSONArray jb = new JSONArray(commentList);
		PrintWriter pw = resp.getWriter();
		pw.println(jb.toString());
		
	}*/
}
