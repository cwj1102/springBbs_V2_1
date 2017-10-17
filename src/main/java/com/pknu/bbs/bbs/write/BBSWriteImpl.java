package com.pknu.bbs.bbs.write;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pknu.bbs.bbs.common.FileSaveHelper;
import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.dto.UploadDto;
@Service
public class BBSWriteImpl implements BBSWrite {
	
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
			commonFileUpload(mfile,articleNum);
		}
		
	}
	
	@Override
	public void write1(BBSDto article, List<MultipartFile> mfile) {
		// TODO Auto-generated method stub
		System.err.println("write1½ÇÇà!");
		if(mfile.get(0).isEmpty()) {
			bbsdao.write1(article);
		} else {
			
			article.setFileStatus((byte)1);
			bbsdao.write1(article);
			commonFileUpload(mfile, article.getArticleNum());
		}
	}
	
	@Override
	public void commonFileUpload(List<MultipartFile> mfile, int articleNum) {
		UploadDto uploadDto = null;
		
		for(MultipartFile uploadFile:mfile) {
			if(!uploadFile.isEmpty()) {
				String storedFname = fileSaveHelper.save(uploadFile);
				
				uploadDto = new UploadDto();
				uploadDto.setOriginFname(uploadFile.getOriginalFilename());
				uploadDto.setStoredFname(storedFname);
				uploadDto.setFileLength(uploadFile.getSize());
				uploadDto.setArticleNum(articleNum);

				bbsdao.insertFile(uploadDto);
			}
		}
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
