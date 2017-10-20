package com.pknu.bbs.bbs.write;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pknu.bbs.bbs.common.FileSaveHelper;
import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
import com.pknu.bbs.upload.UploadFileUtils;
@Service
public class BBSWriteImpl implements BBSWrite {
	
	@Resource(name="saveDir")
	private String saveDir;
	
	@Autowired
	private BBSDao bbsdao;
	
//	@Autowired
//	private FileSystemResource fileSystemResource;
	
	@Autowired
	private FileSaveHelper fileSaveHelper;
	
	@Override
	public void write(BBSDto article, MultipartHttpServletRequest mRequest) {
		
		List<MultipartFile> mfile = mRequest.getFiles("fileData");
		System.out.println(article);
		if(mfile.get(0).isEmpty()) {
			bbsdao.write(article);
		} else {
			int articleNum = bbsdao.getNextArticleNum();
			article.setArticleNum(articleNum);
			article.setFileStatus((byte)1);
			bbsdao.write(article);
//			commonFileUpload(mfile,articleNum);
		}
		
	}
	@Override
	@ResponseBody
	public void write1(BBSDto article, String storedF) {
//	public void write1(BBSDto article, List<MultipartFile> mFile, String storedF) {
		// TODO Auto-generated method stub
		System.err.println("write1½ÇÇà!");
		if(storedF.isEmpty()) {
			bbsdao.write1(article);
		} else {
			
			article.setFileStatus((byte)1);
			bbsdao.write1(article);
			System.out.println(bbsdao.getTotalCount());
			commonFileUpload(storedF, article.getArticleNum());
		}
	}
	
	@Override
	public void commonFileUpload(String storedF, int articleNum) {
		UploadDto uploadDto = null;
		ArrayList<String> storedList = new ArrayList<>();
		
		StringTokenizer st = new StringTokenizer(storedF,","); 
		System.out.println(st.countTokens()); 
		while(st.hasMoreTokens()) {
			String realName = st.nextToken();
			String storedName = realName.substring(12);
			String storedPath = UploadFileUtils.calcPath(saveDir);
			File file = new File(saveDir + storedPath, storedName);
			System.out.println(file.length());
			System.out.println(storedName);
			System.out.println(storedName.substring(storedName.lastIndexOf(".")+1));
			System.out.println(storedName.substring(0, 2));
			if(storedName.substring(0, 2).equals("s_")) {
				continue;
			}
			
			
			uploadDto = new UploadDto();
			
			uploadDto.setOriginFname(storedName.substring(37));
			uploadDto.setStoredFname(storedName);
			uploadDto.setFileLength(file.length());
			uploadDto.setArticleNum(articleNum);

			bbsdao.insertFile(uploadDto);
			
			
			storedList.add(storedName);
		} 
		/*for(String uploadFile:storedList) {
			if(!uploadFile.isEmpty()) {
				
				uploadDto = new UploadDto();
				
				uploadDto.setOriginFname(uploadFile.substring(37));
				uploadDto.setStoredFname(uploadFile);
				uploadDto.setFileLength(uploadFile.getSize());
				uploadDto.setArticleNum(articleNum);

				bbsdao.insertFile(uploadDto);
			}
		}*/
		/*
		String uuid = UUID.randomUUID().toString();
			
		String originFname = mfile.getOriginalFilename();
		String storedFname = uuid + "_" + originFname;
			
		String savePath = fileSystemResource.getPath()+storedFname;
			
		long fileSize = mfile.getSize();
			
		try {
			mfile.transferTo(new File(savePath));
		} catch (IllegalStateException | IOException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("originFname", originFname);
		hm.put("storedFname", storedFname);
		hm.put("fileSize", fileSize);
		hm.put("articleNum", articleNum);
		
		bbsdao.fileUpload(hm);
*/
	}

	@Override
	@Transactional(readOnly = false)
	public String write(BBSDto article){
		System.out.println(article);
		try {
			bbsdao.write(article);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/list.bbs?pageNum=1";
	}

/*	@Transactional(readOnly = false)
	@Override
	public void fileUpload(String originFname, String storedFname, long fileSize, String articleNum) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("originFname", originFname);
		hm.put("storedFname", storedFname);
		hm.put("fileSize", fileSize);
		hm.put("articleNum", articleNum);
		
		bbsdao.fileUpload(hm);
	}*/
}
