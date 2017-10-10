package com.pknu.bbs.bbs.join;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pknu.bbs.bbs.dao.BBSDao;

@Service
public class BBSJoinImpl implements BBSJoin {

	@Autowired
	BBSDao bbsdao;
	
	@Override
	public void join(Model model,String id, String pass) {
		
		HashMap<Object, Object> paramMap = new HashMap<>();
		
		String result;
		result = bbsdao.joinCheck(id);
		if(result!=null) {
			result="중복된 아이디 입니다.";
		} else {
			paramMap.put("id", id);
			paramMap.put("pass", pass);
			
			bbsdao.join(paramMap);
			result = "회원가입이 되었습니다.";
		}
		model.addAttribute("result", result);
		
	}
	
}
