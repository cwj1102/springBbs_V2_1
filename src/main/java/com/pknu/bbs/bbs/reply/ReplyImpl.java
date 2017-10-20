package com.pknu.bbs.bbs.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;
import com.pknu.bbs.bbs.write.BBSWrite;



@Service
public class ReplyImpl implements BBSReply {
	
	@Autowired
	BBSDao bbsdao;
	
	@Autowired
	BBSWrite bbswrite;
	
	@Override
	public void reply(Model model, BBSDto article, String id, List<MultipartFile> mfile){
		article.setId(id);
		System.out.println(article);
		System.err.println("reply1½ÇÇà!");
		if(mfile.get(0).isEmpty()) {
			bbsdao.reply(article);
//			bbsdao.write1(article);
		} else {
			
			article.setFileStatus((byte)1);
			bbsdao.reply(article);
//			bbsdao.write1(article);
//			bbswrite.commonFileUpload(mfile, article.getArticleNum());
		}
	}

}
