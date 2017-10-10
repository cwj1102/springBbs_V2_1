package com.pknu.bbs.bbs.reply;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.ui.Model;

import com.pknu.bbs.bbs.dto.BBSDto;

public interface BBSReply {
	void reply(Model model, BBSDto article/*String depth, String articleNum,String pos, String groupId, String title, String content*/, String id)
			throws ServletException, IOException;
}
