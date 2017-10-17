package com.pknu.bbs.bbs.reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pknu.bbs.bbs.dao.BBSDao;
import com.pknu.bbs.bbs.dto.BBSDto;



@Service
public class ReplyImpl implements BBSReply {
	
	@Autowired
	BBSDao bbsdao;
	
	
	@Override
	public void reply(Model model, BBSDto article, String id){
		article.setId(id);
		System.out.println(article);
		bbsdao.reply(article);
	}

}
