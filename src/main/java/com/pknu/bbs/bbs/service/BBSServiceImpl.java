package com.pknu.bbs.bbs.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pknu.bbs.bbs.common.Page;
import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
import com.pknu.bbs.comment.dao.CommentDao;

@Service
public class BBSServiceImpl implements BBSService {
	@Autowired
	BBSDao bbsDao;
	
//	@Autowired
//	CommentDao commentDao;
//	
	@Autowired //type이 Page인 빈은 DI해준다
	Page page;
	
	
	@Resource(name="pageSize") //type이 String인 빈을 DI해준다 그중에서 id->name->class 순으로 "pageSize"인 녀석을 DI한다.
	Integer pageSize;
	
	@Resource(name="pageBlock")
	Integer pageBlock;
	
//	@Autowired
//	private FileSystemResource fileSystemResource;
	
	@Override
	public void list(int pageNum, Model model) {
		int totalCount=0;
		ArrayList<BBSDto> articleList=null;
		HashMap<String, String> pagingMap=null;
		
		
		
		
		
		try {
			totalCount = bbsDao.getTotalCount();
			
			pagingMap = page.paging(pageNum, totalCount, pageSize, pageBlock);
			
			int startRow = page.getStartRow();
			int endRow = page.getEndRow();
			HashMap<Object,Object> paramMap = new HashMap<>();
			paramMap.put("startRow", startRow);
			paramMap.put("endRow", endRow);
			
			articleList = (ArrayList<BBSDto>)bbsDao.getArticleList(paramMap);
			
			for(BBSDto bbsdto:articleList) {
				bbsdto.setCommentCount((long)bbsDao.commentsCount(bbsdto.getArticleNum()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		model.addAttribute("totalCount",totalCount);
		model.addAttribute("articleList",articleList);
		model.addAttribute("pageCode",pagingMap.get("pageCode"));
	
	}
	
	@Override
	public void content(String pageNum, String articleNum, Model model) {
		ArrayList<UploadDto> uploadList;
		BBSDto article = new BBSDto();
		try {
			article = bbsDao.getContent(articleNum);
			article.setCommentCount((long)bbsDao.commentsCount(Integer.parseInt(articleNum)));
			
			if(article.getFileStatus()==1) {
				uploadList = new ArrayList<>();
				uploadList = (ArrayList<UploadDto>) bbsDao.getFileStatus(articleNum);
				System.out.println(uploadList);
				model.addAttribute("uploadList",uploadList);
			}
			model.addAttribute("article", article);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		model.addAttribute("article", article);
		model.addAttribute("pageNum", pageNum);
	}
	
	@Override
	public void delete(String articleNum) throws ServletException, IOException {
		try {
			bbsDao.delete(articleNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void updateForm(String articleNum, Model model) throws ServletException, IOException {
		
		BBSDto article=null;
		try {
			article=bbsDao.getUpdateArticle(articleNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("article", article);
	}
	
	@Override
	public void update(Model model, String articleNum, String title, String content) throws ServletException, IOException {
		
		System.out.println(articleNum + title + content);
		HashMap<Object,Object> paramMap = new HashMap<>();
		paramMap.put("articleNum", articleNum);
		paramMap.put("title",title);
		paramMap.put("content", content);
		try {
			bbsDao.updateArticle(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Resource(name="saveDir")
	private String saveDir;
	@Override
	public void download(String storedFname, HttpServletResponse resp) {
		UploadDto uploadDto = bbsDao.getDownloadStatus(storedFname);
			try {
				byte fileByte[] = FileUtils.readFileToByteArray(new File(saveDir+storedFname));
				resp.setContentType("application/octet-stream");
				resp.setContentLength(fileByte.length);
				resp.setHeader("Content-Disposition", "attachment; fileName=\""+ URLEncoder.encode(uploadDto.getOriginFname(),"UTF-8")+"\";");
				resp.setHeader("Content-Transfer-Encoding","binary");
				resp.getOutputStream().write(fileByte);
				
				resp.getOutputStream().flush();
				resp.getOutputStream().close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	

}
