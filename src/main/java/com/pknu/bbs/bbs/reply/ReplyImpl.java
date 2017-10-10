package com.pknu.bbs.bbs.reply;

import java.io.IOException;

import javax.servlet.ServletException;

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
	public void reply(Model model, BBSDto article/*String depth, String articleNum,String pos, String groupId, String title, String content*/, String id){
/*		BBSDto article = new BBSDto();
		
		article.setArticleNum(Integer.parseInt(articleNum));
		article.setDepth(Integer.parseInt(depth));
//		article.setPos(Integer.parseInt(pos));
		article.setGroupId(Integer.parseInt(groupId));
		System.out.println(groupId);

		article.setTitle(title);
		article.setContent(content);
*/		article.setId(id);
		System.out.println(article);

//		HashMap<Object,Object> paramMap = new HashMap<>();
//		paramMap.put("pos", article.getPos());
//		paramMap.put("groupId", article.getGroupId());
		
//		bbsdao.posUpdate(paramMap);
		bbsdao.reply(article);
	}

}
