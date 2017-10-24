package com.pknu.bbs.bbs.write;

import java.util.List;

import com.pknu.bbs.bbs.dto.BBSDto;

public interface BBSWrite {

//	void write1(BBSDto article, List<MultipartFile> mfile, String storedF);
	void write(BBSDto article);

	void commonFileUpload(int articleNum, List<String> fileNames);
	
	
}
