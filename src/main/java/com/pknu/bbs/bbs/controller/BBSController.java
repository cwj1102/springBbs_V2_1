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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
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
	 * @ModelAttribute는 파라미터 이름을 반드시 적어주자
	 * 그러면 객체가 아니고 프리미티브 타입이 와도 됨
	 * public String list(@ModelAttribute("pageNum") String pageNum, Model model){
	 * public String list(@ModelAttribute("pageNum") int pageNum, Model model){
	 * 아래코드는 에러가 남... 파라미터 이름을 생략하면 int를 객체로 생성할려고 해서 에러가 남
	 * public String list(@ModelAttribute int pageNum, Model model){
	 * 아래코드는 객체를 생성할려고 하는데  String 이니까 에러는 안나지만... 파라미터가 안넘어옴
	 * public String list(@ModelAttribute String pageNum, Model model){
	 */
	@RequestMapping(value="/list.bbs")
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
	
	@RequestMapping(value="/writeForm.bbs", method=RequestMethod.GET)
	public String writeForm(/*HttpSession session, HttpServletRequest req*/) {
		/*System.out.println(req.getHeader("referer"));
		if((String)session.getAttribute("id")==null){
			req.setAttribute("pageNum", "1");
			return "login";
		}*/
		return "writeForm";
	}
//	value값은 method를 요청하지 않을 경우 굳이 안써도 된다
//	@Transactional(readOnly=false)
/*	@RequestMapping(value="/write.bbs", method=RequestMethod.POST)
	public String write(BBSDto article, HttpSession session, MultipartHttpServletRequest mRequest) {
		article.setId((String)session.getAttribute("id"));
		bbswrite.write(article,mRequest);
		return "redirect://list.bbs?pageNum=1";
	}
*/	@RequestMapping(value="/write.bbs", method=RequestMethod.POST)
	public String write(BBSDto article, HttpSession session, @RequestPart("fileData") List<MultipartFile> mfile) {
		article.setId((String)session.getAttribute("id"));
		if(article.getContent().isEmpty() || article.getTitle().isEmpty()) {
			return "writeForm";
		}
		bbswrite.write1(article,mfile);
		return "redirect://list.bbs?pageNum=1";
	}
	@RequestMapping(value="/download.bbs")
	@ResponseBody
	public FileSystemResource download(@RequestParam String storedFname, 
									@RequestParam String originFname, 
									@RequestParam int fileLength, 
									HttpServletResponse resp) {
		return bbsService.download(resp, storedFname, originFname, fileLength);
	}
	
	
	@RequestMapping(value="/content.bbs")
	public String content(@RequestParam("pageNum") String pageNum, 
			@RequestParam String articleNum, Model model, @RequestParam("fileStatus") int fileStatus, HttpServletRequest req) {
//		model.addAttribute("referer", req.getRequestURL());
		System.err.println(fileStatus);
		bbsService.content(fileStatus, articleNum, model);
		model.addAttribute("pageNum",pageNum);
		return "content";
	}
	@RequestMapping(value="/delete.bbs")
	public String delete(String articleNum,String pageNum) {
			try {
				bbsService.delete(articleNum);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		
		return "redirect:list.bbs?pageNum="+pageNum;
	}
	
	@RequestMapping(value="/update.bbs", method=RequestMethod.GET)
	public String updateForm(@ModelAttribute("articleNum") String articleNum, @ModelAttribute("pageNum") String pageNum, Model model) {
//		model.addAttribute("articleNum", articleNum);
//		model.addAttribute("pageNum", pageNum);
		try {
			bbsService.updateForm(articleNum, model);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return "updateForm";
	}
	
	@RequestMapping(value="/update.bbs", method=RequestMethod.POST)
	public String update(Model model, String pageNum, String articleNum, String title, String content) {
		try {
			bbsService.update(model, articleNum, title, content);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		System.out.println(req.getRequestURI());
		if(req.getRequestURI().equals("/content.bbs")&&!req.getSession().getAttribute("reqFull").equals("")) {
			try {
				System.err.println(req.getSession().getAttribute("reqFull"));
				try {
					bbsLogin.loginCheck(req);
					resp.sendRedirect((String)req.getSession().getAttribute("reqFull"));
					req.getSession().setAttribute("reqFull", "");
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
		}
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
