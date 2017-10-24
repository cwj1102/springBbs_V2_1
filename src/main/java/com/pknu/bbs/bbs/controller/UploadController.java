package com.pknu.bbs.bbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pknu.bbs.bbs.util.MediaUtils;
import com.pknu.bbs.bbs.util.UploadFileUtils;

@Controller
public class UploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Resource(name = "saveDir")
	String saveDir;
/*
	@RequestMapping("/")
	public String main() {
		return "uploadAjax";
	}

*/
	// java8은 스프링 3.2.9부터 지원함 ...하위버전에서는
	// ASM ClassReader failed to parse class file
	// - probably due to a new Java class file version that isn't supported yet:
	// 에러가 발생함

	// List<String> 형태의 JSON으로 리턴 할려면 스프링 3. 버전에서는 아래의 jar 파일인
	// jackson-mapper-asl과 jackson-core-asl만 있어도 처리할 수 있음
	// 그런데 스프링 4. 버전에서는 위의 두 가지 jar로 처리할려면 에러 발생
	// 스프링 3버전과 4버전에서는 jackson-databind 디펜더시만 설정해도 처리할 수 있음
	// 그리고 produces = "appliation/json"으로 설정
	// @ResponseBody
	// @RequestMapping(value ="/uploadAjax", method=RequestMethod.POST,
	// produces = "application/json;charset=UTF-8")
	// public ResponseEntity<List<String>> uploadAjax(MultipartHttpServletRequest
	// mRequest) throws Exception{
	// List<String> fileList= new ArrayList<>();
	// List<MultipartFile> mfile=mRequest.getFiles("multiFile");
	// for(MultipartFile file : mfile){
	// fileList.add(UploadFileUtils.uploadFile(saveDir,file.getOriginalFilename(),file.getBytes()));
	// }
	// return new ResponseEntity<>( fileList, HttpStatus.CREATED);
	// }
	//
	// ResponseEntity를 사용하지 않을때
	@ResponseBody
	@RequestMapping(value = "/uploadAjax.bbs", method = RequestMethod.POST)
	public List<String> uploadAjax(@RequestPart("multiFile") List<MultipartFile> multiFile) throws Exception {
		List<String> fileList = new ArrayList<>();
		for (MultipartFile file : multiFile) {
			fileList.add(UploadFileUtils.uploadFile(saveDir, file.getOriginalFilename(), file.getBytes()));
		}
		return fileList;
	}

	@ResponseBody
	@RequestMapping("/displayFile.bbs")//다운로드
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			MediaType mType = MediaUtils.getMediaType(formatName);
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(saveDir + fileName);
			System.out.println(saveDir + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			} else {
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			}
			fileName = fileName.substring(fileName.indexOf("_") + 1);
			headers.add("Content-Disposition",
					"attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8").replace("+", "%20") + "\"");
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteFile.bbs", method = RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName) {
		logger.info("delete file: " + fileName);
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
		MediaType mType = MediaUtils.getMediaType(formatName);

		if (mType != null) {//이미지 파일은 원본파일도 삭제해야 함(s_가 없는 파일)
			String front = fileName.substring(0, 12);
			String end = fileName.substring(14);
			new File(saveDir + (front + end).replace('/', File.separatorChar)).delete();
		}

		new File(saveDir + fileName.replace('/', File.separatorChar)).delete();
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAllFiles.bbs", method = RequestMethod.POST)
	// ajax() 함수가 배열을 직렬화 하지 않고 보낼때는 아래 코드처럼 해도 처리됨
	// public ResponseEntity<String> deleteFile(@RequestParam("files[]") String[] files) {
	public ResponseEntity<String> deleteFile(@RequestParam("files") String[] files) {
		logger.info("delete all files: " + files);
		if (files == null || files.length == 0) {
			return new ResponseEntity<String>("deleted", HttpStatus.OK);
		}
		for (String fileName : files) {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			MediaType mType = MediaUtils.getMediaType(formatName);
			if (mType != null) {
				String front = fileName.substring(0, 12);
				String end = fileName.substring(14);
				new File(saveDir + (front + end).replace('/', File.separatorChar)).delete();
			}
			new File(saveDir + fileName.replace('/', File.separatorChar)).delete();
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
}
