package com.pknu.bbs.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {
	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	public static String uploadFile(String saveDir, String originFname, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();
		String storedFname = uid.toString() + "_" + originFname;
		String storedPath = calcPath(saveDir);
		File target = new File(saveDir + storedPath, storedFname);
		FileCopyUtils.copy(fileData, target);
		String formatName = originFname.substring(originFname.lastIndexOf(".") + 1);
		String uploadedFileName = null;

		if (MediaUtils.getMediaType(formatName) != null) {
			uploadedFileName = makeThumbnail(saveDir, storedPath, storedFname);
		} else {
			uploadedFileName = makeIcon(saveDir, storedPath, storedFname);
		}
		return uploadedFileName;
	}

	public static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);

		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

		makeDir(uploadPath, yearPath, monthPath, datePath);
		logger.info(datePath);

		return datePath;
	}

	private static String makeThumbnail(String saveDir, String storedPath, String storedFname) throws Exception {
		BufferedImage sourceImg = ImageIO.read(new File(saveDir + storedPath, storedFname));

		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		String thumbnailName = saveDir + storedPath + File.separator + "s_" + storedFname;
		File newFile = new File(thumbnailName);
		String formatName = storedFname.substring(storedFname.lastIndexOf(".") + 1);

		ImageIO.write(destImg, formatName.toUpperCase(), newFile);
		return thumbnailName.substring(saveDir.length()).replace(File.separatorChar, '/');//웹으로 보내기 위해
	}//이미지는 s_를 넘겨준다.

	private static String makeIcon(String saveDir, String storedPath, String storedFname) throws Exception {
		String iconName = saveDir + storedPath + File.separator + storedFname;
		return iconName.substring(saveDir.length()).replace(File.separatorChar, '/');
	}//이미지 외에는 uuid가 붙은 파일을 보내준다.

	private static void makeDir(String uploadPath, String... paths) {//@Schadular로 예약 가능
		if (new File(paths[paths.length - 1]).exists()) {//paths.length=3 paths[2]가 마지막 폴더
			return;
		}

		for (String path : paths) {
			File dirPath = new File(uploadPath + path);
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

}
