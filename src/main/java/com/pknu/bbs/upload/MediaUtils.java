package com.pknu.bbs.upload;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtils {
	private static Map<String, MediaType> mediaMap;

	static {//인스턴스를 하나만 만들기 위해
		mediaMap = new HashMap<String, MediaType>();//인스턴스화
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
		mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
	}

	public static MediaType getMediaType(String type) {
		return mediaMap.get(type.toUpperCase());
	}
}
