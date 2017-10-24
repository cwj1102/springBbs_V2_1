package com.pknu.bbs.bbs.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.pknu.bbs.bbs.common.Page;
import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
import com.pknu.bbs.bbs.util.MediaUtils;
import com.pknu.bbs.bbs.util.UploadFileUtils;
import com.pknu.bbs.bbs.write.BBSWrite;

@Service
public class BBSServiceImpl implements BBSService {
	@Autowired
	BBSDao bbsDao;

	// @Autowired
	// CommentDao commentDao;
	//
	@Autowired // type이 Page인 빈은 DI해준다
	Page page;

	@Resource(name = "pageSize") // type이 String인 빈을 DI해준다 그중에서 id->name->class 순으로 "pageSize"인 녀석을 DI한다.
	Integer pageSize;

	@Resource(name = "pageBlock")
	Integer pageBlock;

	@Autowired
	BBSWrite bbsWrite;
	
	// @Autowired
	// private FileSystemResource fileSystemResource;
	@Override
	public void list(int pageNum, Model model) {
		int totalCount = 0;
		ArrayList<BBSDto> articleList = null;
		HashMap<String, String> pagingMap = null;

		try {
			totalCount = bbsDao.getTotalCount();

			pagingMap = page.paging(pageNum, totalCount, pageSize, pageBlock);

			int startRow = page.getStartRow();
			int endRow = page.getEndRow();
			HashMap<Object, Object> paramMap = new HashMap<>();
			paramMap.put("startRow", startRow);
			paramMap.put("endRow", endRow);

			articleList = (ArrayList<BBSDto>) bbsDao.getArticleList(paramMap);

			for (BBSDto bbsdto : articleList) {
				bbsdto.setCommentCount((long) bbsDao.commentsCount(bbsdto.getArticleNum()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("articleList", articleList);
		model.addAttribute("pageCode", pagingMap.get("pageCode"));

	}

	@Override
	public void content(int fileStatus, String articleNum, Model model) {
		// ArrayList<UploadDto> uploadList;
		BBSDto article = null;
		try {
			article = bbsDao.getContent(articleNum);
			article.setCommentCount((long) bbsDao.commentsCount(Integer.parseInt(articleNum)));

			if (article.getFileStatus() == 1) {
				/*
				 * uploadList = new ArrayList<>(); uploadList = (ArrayList<UploadDto>)
				 * bbsDao.getFileStatus(articleNum); System.out.println(uploadList);
				 * model.addAttribute("uploadList",uploadList);
				 */
				model.addAttribute("uploadList", bbsDao.getFileStatus(articleNum));
			}
			model.addAttribute("article", article);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String articleNum,int fileStatus) throws ServletException, IOException {
		try {
			if(fileStatus==1) {
				deleteFile(articleNum);
				bbsDao.delete(articleNum);
			} else {
				
				bbsDao.delete(articleNum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deleteFile(String articleNum){
		System.out.println("delete : articleNum" + articleNum);
		List<String> storedFnameList = bbsDao.getStoredFnames(Integer.parseInt(articleNum));
		System.out.println("storedFnameList" + storedFnameList);
		if(!storedFnameList.isEmpty()){
			for(String storedFname : storedFnameList){
				File file = new File(saveDir+storedFname);
				if(file.exists()){
					file.delete();
				}
			}
		}
	}

	@Override
	public void updateForm(String articleNum, int fileStatus, Model model) throws ServletException, IOException {

		model.addAttribute("article", bbsDao.getUpdateArticle(articleNum));
		if(fileStatus==1) {
			List<String> fileList = bbsDao.getFileStatus(articleNum);
			model.addAttribute("fileList", fileList);
			model.addAttribute("fileCount", fileList.size());
		} else {
//			기존의 글이 파일 업로드가 없는 글이었을 경우는 updateForm.jsp의 fileCount에 공백값이
//			들어가므로 update.bbs 요청시 400에러가 남...그래서 편법으로 0을 주었음
			model.addAttribute("fileCount", 0);
		}
	}

/*	@Override
	public void update(Model model, String articleNum, String title, String content)
			throws ServletException, IOException {

		System.out.println(articleNum + title + content);
		HashMap<Object, Object> paramMap = new HashMap<>();
		paramMap.put("articleNum", articleNum);
		paramMap.put("title", title);
		paramMap.put("content", content);
		try {
			bbsDao.updateArticle(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public void update(BBSDto article, String[] deleteFileNames, Model model, int fileCount) {
		if(deleteFileNames != null ) {
			ArrayList<String> delFileList = new ArrayList<>();
			for(String delFileName : deleteFileNames) {
//				Mybatis 매퍼 파일은 List만 받을 수 있음(String 배열은 받을 수 없다)
				delFileList.add(delFileName);
			}
			bbsDao.dbDelFileName(delFileList);
			for(String deleteFname : deleteFileNames) {
				storageDelFileName(deleteFname);
			}
		}
		
		if(article.getFileNames() == null) {
			if(deleteFileNames != null) {
				if(fileCount == deleteFileNames.length) {
					article.setFileStatus((byte)0);
					System.err.println("article.setFileStatus((byte)0);");
				}
			}
		} else {
			/*if(deleteFileNames.length != 0){				
				bbsDao.dbDelFileName(deleteFileNames);
				for(String deleteFname : deleteFileNames){
					storageDelFileName(deleteFname);
				}
			}*/
			article.setFileStatus((byte)1);					
			bbsWrite.commonFileUpload(article.getArticleNum(),article.getFileNames());
						
		}
		System.out.println("fileStatus : " + article.getFileStatus());
		bbsDao.updateArticle(article);
//		model.addAttribute("articleNum", article.getArticleNum());
		model.addAttribute("fileStatus", article.getFileStatus());		
	} 
	
	public void storageDelFileName(String deleteFname){
		if(deleteFname!=null){
			File file = new File((saveDir+deleteFname).replace('/', File.separatorChar));
			System.out.println("file.toString() : " + file.toString());
			if(file.exists()){
				file.delete();
			}
			String fileFormat = deleteFname.substring(deleteFname.lastIndexOf(".")+1);
			MediaType mType = MediaUtils.getMediaType(fileFormat);
			if(mType!=null) {
				String front = deleteFname.substring(0, 12);
				String end = deleteFname.substring(12);
				System.out.println("saveDir + front + \"s_\" + end" + saveDir + front + "s_" + end);
				new File((saveDir + front + "s_" + end).replace('/', File.separatorChar)).delete();
				System.out.println((saveDir + front + "s_" + end).replace('/', File.separatorChar));
			}
		}
		
		
	}

	@Resource(name = "saveDir")
	private String saveDir;

	@Override
	public void download(String storedFname, HttpServletResponse resp) {
		UploadDto uploadDto = bbsDao.getDownloadStatus(storedFname);
		try {

			String storedPath = UploadFileUtils.calcPath(saveDir);
			System.out.println(saveDir + storedPath + storedFname);
			byte fileByte[] = FileUtils.readFileToByteArray(new File(saveDir + storedPath + storedFname));
			resp.setContentType("application/octet-stream");
			resp.setContentLength(fileByte.length);
			resp.setHeader("Content-Disposition",
					"attachment; fileName=\"" + URLEncoder.encode(uploadDto.getStoredFname().substring(uploadDto.getStoredFname().indexOf("_")+1), "UTF-8") + "\";");
			resp.setHeader("Content-Transfer-Encoding", "binary");
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

	@Override
	public FileSystemResource download(HttpServletResponse resp, String storedFname) {
		resp.setContentType("application/download");
		
//		resp.setContentLength(fileLength);
		String originFname = storedFname.substring(storedFname.indexOf("_")+1);
		try {
			originFname = URLEncoder.encode(originFname, "utf-8").replace("+", "%20").replace("%28", "(").replace("%29",
					")");
		} catch (Exception e) {	}

		resp.setHeader("Content-Disposition", "attachment;" + " filename=\"" + originFname + "\";");
		
		File file = new File(saveDir+storedFname);
		resp.setContentLength((int)file.length());
		System.out.println((int)file.length());
		
		FileSystemResource fsr = new FileSystemResource(saveDir + storedFname);
		System.out.println(saveDir + storedFname);
		return fsr;
		
		/*
		resp.setContentType("application/download");	
		String originFname=storedFname.substring(storedFname.indexOf("_")+1);;
		try{
			originFname = URLEncoder.encode(originFname,"utf-8").replace("+","%20").replace("%28", "(").replace("%29", ")");
		}catch(Exception e){

		}

		resp.setHeader("Content-Disposition", "attachment;" +" filename=\""+originFname+ "\";");
		FileSystemResource fsr= new FileSystemResource(saveDir+storedFname);
		return fsr;*/
	}

}
