package com.pknu.bbs.comment.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pknu.bbs.comment.dto.CommentDto;
import com.pknu.bbs.comment.service.CommentService;

@Controller
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	
	@RequestMapping(value="/commentRead.comment")
	@ResponseBody
	public List<CommentDto> commentRead(@RequestParam("articleNum") int articleNum, @RequestParam("commentRow") int commentRow) {
		return commentService.getComments(articleNum, commentRow);
	}
	
//	MappingJackson2JsonView를 이용하기
/*	@RequestMapping(value="/commentRead.comment")
	public String commentRead(@RequestParam("articleNum") int articleNum, @RequestParam("commentRow") int commentRow, Model model) {
//		System.out.println("json");
		model.addAttribute("commentList",commentService.getComments(articleNum, commentRow));
//		System.out.println(commentService.getComments(articleNum, commentRow));
		return "JSON";
	}*/
		
	@RequestMapping(value="/commentWrite.comment")
	public @ResponseBody HashMap<String, Object> commentWrite(CommentDto comment, HttpSession session) {
		//@ResponseBody는 보낼때 json형식으로 보낸다는 약속
		System.out.println("컴맨트 " + session.getAttribute("id"));
		comment.setId((String)session.getAttribute("id"));
		commentService.insertComment(comment);
		List<CommentDto> commentList = commentService.getComments(comment.getArticleNum(),10);
		
		HashMap<String,Object> hm = new HashMap<>();
		hm.put("result", 1);
		hm.put("commentList", commentList);
		return hm;
		
		/*try {
			bbscomment.write(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;*/
	}
	/*
	@RequestMapping(value="/logout.bbs")
	public String logout(HttpSession session, String pageNum) {
		req.getSession().setAttribute("id", null);
		session.invalidate();
		return "redirect:list.bbs?pageNum="+pageNum;
	}
*/
}
