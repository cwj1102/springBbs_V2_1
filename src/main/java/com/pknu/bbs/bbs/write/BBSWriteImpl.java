package com.pknu.bbs.bbs.write;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
@Service
public class BBSWriteImpl implements BBSWrite {
	
	@Autowired
	BBSDao bbsdao;
	
	@Override
	@Transactional(readOnly = false)
	public String write(BBSDto article) throws ServletException, IOException {
		System.out.println(article);
		try {
			bbsdao.write(article);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/list.bbs?pageNum=1";
	}

	@Transactional(readOnly = false)
	@Override
	public void fileUpload(String originFname, String storedFname, long fileSize, String articleNum) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("originFname", originFname);
		hm.put("storedFname", storedFname);
		hm.put("fileSize", fileSize);
		hm.put("articleNum", articleNum);
		
		bbsdao.fileUpload(hm);
	}
}
