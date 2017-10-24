package com.pknu.bbs.bbs.write;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
@Service
public class BBSWriteImpl implements BBSWrite {
	
	@Resource(name="saveDir")
	private String saveDir;
	
	@Autowired
	private BBSDao bbsdao;

	@Override
	@ResponseBody
	public void write(BBSDto article) {
//	public void write1(BBSDto article, List<MultipartFile> mFile, String storedF) {
		// TODO Auto-generated method stub
		System.err.println("write1½ÇÇà!");
		if(article.getFileNames()==null) {
			bbsdao.write(article);
		} else {
			article.setFileStatus((byte)1);
			bbsdao.write(article);
			commonFileUpload(article.getArticleNum(), article.getFileNames());
		}
	}
	
	@Override
	public void commonFileUpload(int articleNum, List<String> fileNames) {
		UploadDto uploadDto = null;

		for(String storedFname : fileNames) {
			uploadDto = new UploadDto();
			uploadDto.setStoredFname(storedFname);
			uploadDto.setArticleNum(articleNum);
			bbsdao.insertFile(uploadDto);
			
		} 
		
	}

}
