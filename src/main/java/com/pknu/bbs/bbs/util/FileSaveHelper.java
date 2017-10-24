package com.pknu.bbs.bbs.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class FileSaveHelper {
	@Resource(name="saveDir")
	private String saveDir;
	
	public String save(MultipartFile mfile) {
		String storedFname = UUID.randomUUID().toString() + "_" + mfile.getOriginalFilename();
			
		String savePath = saveDir+storedFname;
			try {
				mfile.transferTo(new File(savePath));
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return storedFname;
		}
	}
