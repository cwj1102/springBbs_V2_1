package com.pknu.bbs.bbs;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pknu.bbs.bbs.dao.BBSDao;

		@RunWith(SpringJUnit4ClassRunner.class)
		//������ 4 ���ʹ� Ŭ���� ���̽��� �׽�Ʈ�� ������
		//Spring 4.0.4 context loaders may choose to support path-based and class-bassed resources simultaneously
		//�̷��Ե� ��� ����
		//@ContextConfiguration(classes = {AppConfig.class})
		
		@WebAppConfiguration
		@ContextConfiguration(locations= {"classpath:spring/root-context.xml","classpath:spring/bbs-context.xml"})
		//@ContextConfiguration(locations=("classpath:/spring/*.xml)
		//@ContextConfiguration(locations=("filesrc/main/webapp/WEB-INF/spring/**/*.xml))<=������ ����
public class TestController{
	private static final Logger logger = LoggerFactory.getLogger("TestController.class");
	@Autowired
	BBSDao bbsDao;
	
	@Before
	public void human() {
		logger.info("������");
	}
	@Test
	public void test() {
		logger.info("�� ���� ������ : " + bbsDao.getTotalCount());
	}
}

		
