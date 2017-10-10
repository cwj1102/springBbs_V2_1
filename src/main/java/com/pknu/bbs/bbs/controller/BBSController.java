package com.pknu.bbs.bbs.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
	
	@Autowired
	private FileSystemResource fileSystemResource;
	
	@RequestMapping(value="/list.bbs")
		public String list(int pageNum, Model model) {
		bbsService.list(pageNum, model);
		model.addAttribute("pageNum",pageNum);
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
	public String writeForm(HttpSession session, HttpServletRequest req) {
		if((String)session.getAttribute("id")==null){
			req.setAttribute("pageNum", "1");
			return "login";
		}
		return "writeForm";
	}
//	value값은 method를 요청하지 않을 경우 굳이 안써도 된다
	@Transactional(readOnly=false)
	@RequestMapping(value="/write.bbs", method=RequestMethod.POST)
	public String write(BBSDto article, HttpSession session, /*UploadDto uploadDto, */BindingResult result, MultipartHttpServletRequest mphsr) {
//		매개변수로 DTO를 받아오면  Spring에서는 저절로 jsp에 있는 값중 DTO에 있는 파라미터와 일치하는 값이 저절로 DTO 안에 값이 넣어진체 넘어온다. 		
		System.out.println(article);
		article.setId((String)session.getAttribute("id"));
		
		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.err.println("Error:" + error.getCode() + " - " + error.getDefaultMessage());
			}
			return "writeForm";
		}
		List<MultipartFile> mf = mphsr.getFiles("fileData");
		
		if(mf.size() == 1&& mf.get(0).getOriginalFilename().equals("")) {
			try {
				bbswrite.write(article);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			article.setFileStatus(1);
			try {
				bbswrite.write(article);
			for(int i =0; i<mf.size(); i++) {
				String articleNum = String.valueOf(article.getArticleNum());
					String uuid = UUID.randomUUID().toString();
					
					String originFname = mf.get(i).getOriginalFilename();
					String storedFname = uuid + "_" + originFname;
					
					String savePath = fileSystemResource.getPath()+storedFname;
					
					long fileSize = mf.get(i).getSize();
					
					try {
						mf.get(i).transferTo(new File(savePath));
					} catch (IllegalStateException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					bbswrite.fileUpload(originFname, storedFname, fileSize, articleNum);
		}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
/*
		if(!uploadDto.getFileData().isEmpty()) {
			String originFname = uploadDto.getFileData().getOriginalFilename();
			String imgExt = originFname.substring(originFname.lastIndexOf(".") + 1, originFname.length());
			System.out.println(originFname);
			System.out.println(System.currentTimeMillis());
			for (int i=0;i<10;i++){
	            System.out.println(UUID.randomUUID().toString());
//	            System.out.println(UUID.randomUUID().toString().replace("-", ""));
	        }
			try {
				String uuid = UUID.randomUUID().toString();
				String storedFname = uuid + "_" + originFname;
				
				uploadDto.setFileLength(uploadDto.getFileData().getSize());
				uploadDto.setOriginFname(originFname);
				uploadDto.setStoredFname(storedFname);
				
				byte[] bytes = uploadDto.getFileData().getBytes();
				File outFileName = new File(fileSystemResource.getPath()+storedFname);
				System.out.println(outFileName);
				FileOutputStream fos = new FileOutputStream(outFileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(bytes);
				bos.close();
				fos.close();
			} catch (IOException e) {
				System.err.println("File writing error!");
			}
			System.err.println("File upload success!");
			article.setFileStatus(1);
			bbswrite.writeUpload(article,uploadDto);
		}else {
		
		
		
		try {
			bbswrite.write(article);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		}//else end
		*/
		return "redirect:list.bbs?pageNum=1";
	}
	@RequestMapping(value="/download.bbs")
	public void download(String storedFname, HttpServletResponse resp) {
		System.out.println(storedFname);
		bbsService.download(storedFname, resp);
	}
	
	
	@RequestMapping(value="/content.bbs")
	public String content(@RequestParam("pageNum") String pageNum, 
			@RequestParam String articleNum, Model model) {
		bbsService.content(pageNum, articleNum, model);
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
	public String updateForm(String articleNum, String pageNum, Model model) {
		model.addAttribute("articleNum", articleNum);
		model.addAttribute("pageNum", pageNum);
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
	public String replyForm(BBSDto bd, Model model,/*HttpServletRequest req, */String pageNum/*, String depth, String pos, String groupId*/) {
		/*BBSDto bd = new BBSDto();
		bd.setDepth(Integer.parseInt(req.getParameter("depth")depth));
		bd.setPos(Integer.parseInt(req.getParameter("pos")pos));
		bd.setGroupId(Integer.parseInt(req.getParameter("groupId")groupId));
		*/
		/*req.setAttribute("replyDto", bd);
		req.setAttribute("pageNum", pageNum);*/
		/*System.out.println("뎁스 : " + bd.getDepth());
		System.out.println(bd);*/
		System.out.println(bd);
		model.addAttribute("replyDto", bd);
		model.addAttribute("pageNum", pageNum);
		return "replyForm"; 
	}
	
	@RequestMapping(value="/reply.bbs", method=RequestMethod.POST)
	public String reply(String pageNum, Model model, BBSDto article,/*String articleNum, String depth, String pos, String groupId, String title, String content, */HttpSession session) {
		String id = (String)session.getAttribute("id");
		try {
			bbsreply.reply(model, article/*articleNum, depth, pos, groupId, title, content*/, id);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:list.bbs?pageNum="+pageNum;
	}
	
	@RequestMapping(value="/login.bbs", method=RequestMethod.POST)
	public String login(HttpServletRequest req) {
			
		String view=null;
		try {
			view = bbsLogin.loginCheck(req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return view;
	}
	
/*	@ResponseBody
	@RequestMapping(value="/commentRead.bbs")
	public String commentRead(HttpServletRequest req, HttpServletResponse resp) {
		try {
			bbscomment.read(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value="/commentWrite.bbs")
	public String commentWrite(HttpServletRequest req, HttpServletResponse resp) {
		try {
			bbscomment.write(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	*/
	@RequestMapping(value="/logout.bbs")
	public String logout(HttpSession session, String pageNum) {
		session.invalidate();
		return "redirect:list.bbs?pageNum="+pageNum;
	}

}
