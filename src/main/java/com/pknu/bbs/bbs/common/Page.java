package com.pknu.bbs.bbs.common;

import java.util.HashMap;

import org.springframework.stereotype.Component;
//@Named
@Component
public class Page {
//	private static Page page = new Page();
//	private Page(){}
	
	private int startRow, endRow;
	private StringBuffer sb;
	
//	public synchronized static Page getInstance(){
//		if(page==null){
//			page= new Page();
//		}
//		return page;
//	}
//	Ω∫«¡∏µ¿Œ µ∆˙∆Æ∞° ΩÃ±€≈Ê¿Ã¥Ÿ. ΩÃ±€≈Ê ∆–≈œ¿ª 
	
	
	public synchronized HashMap<String, String> paging(int pageNum, int totalCount, int pageSize, int pageBlock){
		HashMap<String, String> pagingValue = new HashMap<String, String>();
		
		int totalPage = (int) Math.ceil((double)totalCount/pageSize);
		startRow = (pageNum - 1) * pageSize+1;
		endRow = pageNum * pageSize;		
		
		pagingValue.put("startRow", String.valueOf(startRow));
		pagingValue.put("endRow", String.valueOf(endRow));
		
		int startPage = (int)((pageNum-1)/pageBlock)*pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
				
		if(endPage > totalPage) {
			endPage = totalPage;
		}			

		sb = new StringBuffer();
		if(startPage < pageBlock) {
			sb.append("<img src='images/hot.png' width='30' height='9'>");			
		} else {
			sb.append("<img src='images/hot.png' width='30' height='9'");
			sb.append(" onclick='location.href=\"list.bbs?pageNum=");
			sb.append(startPage - pageBlock);
			sb.append("\"' style='cursor:pointer'> ");
		}
		
		sb.append("&nbsp;&nbsp;|");
		for(int i = startPage; i <= endPage; i++) {		
			if(i == pageNum) {
				sb.append("&nbsp;&nbsp;<b><font color='#91B7EF'>");
				sb.append(i);
				sb.append("</font></b>");
			} else {
				sb.append("&nbsp;&nbsp;<a href='list.bbs?pageNum=");
				sb.append(i);
				sb.append("'>");
				sb.append(i);
				sb.append("</a>");
			}
		}
		
		sb.append("&nbsp;&nbsp;|");		
		if(endPage < totalPage) {
			sb.append("<img src='images/hot.png' width='30' height='9'");
			sb.append(" onclick='location.href=\"list.bbs?pageNum=");
			sb.append(startPage + pageBlock);
			sb.append("\"' style='cursor:pointer'> ");
		} else {
			sb.append("<img src='images/hot.png' width='30' height='9'>");			
		}
		
		pagingValue.put("pageCode", sb.toString());
		return pagingValue;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public StringBuffer getSb() {
		return sb;
	}

}
