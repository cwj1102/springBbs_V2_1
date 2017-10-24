package com.pknu.bbs.bbs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.join.BBSJoin;
import com.pknu.bbs.bbs.login.BBSLogin;
import com.pknu.bbs.bbs.reply.BBSReply;
import com.pknu.bbs.bbs.service.BBSService;
import com.pknu.bbs.bbs.write.BBSWrite;

@Controller
public class BBSController {
	
	@Autowired
	private BBSService bbsService;
	
	@Autowired
	private BBSLogin bbsLogin;
	
	@Autowired
	private BBSWrite bbswrite;
	
	
	@Autowired
	private BBSJoin bbsjoin;
	
	@Autowired
	private BBSReply bbsreply;
	
//	@Autowired
//	private FileSystemResource fileSystemResource;
//	@Autowired
//	private FileSystemResource fileSystemResource;
	/*
	 * @ModelAttribute�� �Ķ���� �̸��� �ݵ�� ��������
	 * �׷��� ��ü�� �ƴϰ� ������Ƽ�� Ÿ���� �͵� ��
	 * public String list(@ModelAttribute("pageNum") String pageNum, Model model){
	 * public String list(@ModelAttribute("pageNum") int pageNum, Model model){
	 * �Ʒ��ڵ�� ������ ��... �Ķ���� �̸��� �����ϸ� int�� ��ü�� �����ҷ��� �ؼ� ������ ��
	 * public String list(@ModelAttribute int pageNum, Model model){
	 * �Ʒ��ڵ�� ��ü�� �����ҷ��� �ϴµ�  String �̴ϱ� ������ �ȳ�����... �Ķ���Ͱ� �ȳѾ��
	 * public String list(@ModelAttribute String pageNum, Model model){
	 */
	@RequestMapping(value="/list.bbs")
	@Transactional
		public String list(@ModelAttribute("pageNum") int pageNum, Model model) {
//		model.addAttribute("pageNum", pageNum);
		bbsService.list(pageNum, model);
		return "list";
		
	}
	
	@RequestMapping(value="/joinForm.bbs")
	public String joinForm(String pageNum, Model model) {
		model.addAttribute("pageNum", pageNum);
		
		return "joinform";
	}
	
	@RequestMapping(value="/join.bbs")
	public String join(Model model,String id, String pass) {
		
		bbsjoin.join(model,id,pass);
		return "redirect:list.bbs?pageNum=1";
	}
	
	@RequestMapping(value="/write.bbs", method=RequestMethod.GET)
	@Transactional
	public String writeForm(HttpSession session, HttpServletRequest req) {
		/*if((String)session.getAttribute("id")==null){
		req.setAttribute("pageNum", "1");
			return "login";
		}*/
		return "writeForm";
	}
//	value���� method�� ��û���� ���� ��� ���� �Ƚᵵ �ȴ�
//	@Transactional(readOnly=false)
/*	@RequestMapping(value="/write.bbs", method=RequestMethod.POST)
	public String write(BBSDto article, HttpSession session, MultipartHttpServletRequest mRequest) {
		article.setId((String)session.getAttribute("id"));
		bbswrite.write(article,mRequest);
		return "redirect://list.bbs?pageNum=1";
	}
*/	@RequestMapping(value="/write.bbs", method=RequestMethod.POST)
	@Transactional
	public String write(BBSDto article, HttpSession session) {
		article.setId((String)session.getAttribute("id"));
		if(article.getContent().isEmpty() || article.getTitle().isEmpty()) {
			return "writeForm";
		}
		bbswrite.write(article);
		
		return "redirect://list.bbs?pageNum=1";
	}
	@RequestMapping(value="/download.bbs")
	@ResponseBody
	public FileSystemResource download(@RequestParam String storedFname, 
									HttpServletResponse resp) {
		return bbsService.download(resp, storedFname);
	}
	
	
	@RequestMapping(value="/content.bbs")
	@Transactional
	public String content(@RequestParam("pageNum") String pageNum, 
			@RequestParam String articleNum, 
			Model model, 
			@RequestParam("fileStatus") int fileStatus, 
			HttpServletRequest req,
			HttpSession session) {
		System.err.println(fileStatus);
		bbsService.content(fileStatus, articleNum, model);
		model.addAttribute("pageNum",pageNum);
		return "content";
	}
	@RequestMapping(value="/delete.bbs")
	public String delete(String articleNum,String pageNum, int fileStatus) {
			try {
				System.out.println("delete : fileStatus = " + fileStatus);
				bbsService.delete(articleNum, fileStatus);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		
		return "redirect:list.bbs?pageNum="+pageNum;
	}
	
	@RequestMapping(value="/update.bbs", method=RequestMethod.GET)
	public String updateForm(@ModelAttribute("articleNum") String articleNum, 
			@ModelAttribute("pageNum") String pageNum, 
			@ModelAttribute("fileStatus") int fileStatus,
			Model model) {
//		model.addAttribute("articleNum", articleNum);
//		model.addAttribute("pageNum", pageNum);
		try {
			bbsService.updateForm(articleNum, fileStatus, model);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return "updateForm";
	}
	
	@RequestMapping(value="/update.bbs", method=RequestMethod.POST)
//	�Ʒ��� ���� ���ÿ��� ������ ��
//	ArrayList<String> storedFnameList, ..set,get �޼ҵ尡 ����
//	UpdateDto�� ���� set,get �޼ҵ带 ����ؾ���
//	jsp���� �������� name �Ӽ��� ������ �Ķ���͸� ���� �� ���� 
	public String update(
			BBSDto article, 
//			UpdateDto updateDto,
//			@RequestParam�� ������ ���� �ݵ�� �Ѿ�;� �Ѵ�.
			String[] deleteFileNames,
			String pageNum, 
			Model model, 
			int fileCount
			) {
		if(article.getFileNames()!=null) {
			for(String storedFname : article.getFileNames()) {
				System.err.println("article.getFileNames() : " + storedFname);
			}
		}
		bbsService.update(article, deleteFileNames, model, fileCount);
		/*for(String storedFname : storedFnameList) {
			System.out.println("StringList[] : " + storedFname);
			
		}*/
		/*for(String storedFname : updateDto.getStoredFnameList()) {
			System.out.println("updateDto : " + storedFname);
		}*/
		
		/*try {
//			bbsService.update(model, articleNum, title, content);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return "redirect:list.bbs?pageNum="+pageNum;
	}
	
	@RequestMapping(value="/replyForm.bbs")
	public String replyForm(BBSDto bd, Model model,@ModelAttribute("pageNum") String pageNum) {
		System.out.println(bd);
		model.addAttribute("replyDto", bd);
		return "replyForm"; 
	}
	
	@RequestMapping(value="/reply.bbs", method=RequestMethod.POST)
	public String reply(String pageNum, Model model, BBSDto article,HttpSession session ,@RequestPart("fileData") List<MultipartFile> mfile) {
		String id = (String)session.getAttribute("id");
		try {
			bbsreply.reply(model, article, id, mfile);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return "redirect:list.bbs?pageNum="+pageNum;
	}
	@RequestMapping(value="/loginForm.bbs")
	public String loginForm(HttpServletRequest req, Model model) {
		return "login";
	}
	@RequestMapping(value="/login.bbs"/*, method=RequestMethod.POST*/)
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		String view=null;
		try {
			view = bbsLogin.loginCheck(req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return view;
	}

	@RequestMapping(value="/logout.bbs")
	public String logout(HttpSession session, String pageNum) {
		session.invalidate();
		return "redirect:list.bbs?pageNum="+pageNum;
	}

}
