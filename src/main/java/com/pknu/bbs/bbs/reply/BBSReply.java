package com.pknu.bbs.bbs.reply;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.pknu.bbs.bbs.dto.BBSDto;

public interface BBSReply {
	void reply(Model model, BBSDto article/*String depth, String articleNum,String pos, String groupId, String title, String content*/, String id, List<MultipartFile> mfile)
			throws ServletException, IOException;
}
