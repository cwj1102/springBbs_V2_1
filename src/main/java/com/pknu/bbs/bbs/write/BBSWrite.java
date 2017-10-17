package com.pknu.bbs.bbs.write;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pknu.bbs.bbs.dto.BBSDto;

public interface BBSWrite {
	String write(BBSDto article);

	void write(BBSDto article, MultipartHttpServletRequest mRequest);

	void write1(BBSDto article, List<MultipartFile> mfile);
	
	void commonFileUpload(List<MultipartFile> mfile, int articleNum);
}
