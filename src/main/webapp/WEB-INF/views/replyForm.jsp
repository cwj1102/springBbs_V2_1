<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>답글쓰기</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<body> 
뎁스:${replyDto.depth }<br>
<%-- 포지션:${replyDto.pos }<br> --%>
페이지넘:${pageNum }<br>
그룹아이디:${replyDto.groupId }<br>
<div class="col-md-3 col-sm-2"></div>
<div class="col-md-6 col-sm-8">
<center>
<form action="/bbs/reply.bbs" method="post" enctype="multipart/form-data">
	<input type="hidden" name="articleNum" value="${replyDto.articleNum}">
	<input type="hidden" name="pageNum" value="${pageNum}">                 
    <input type="hidden" name="depth" value="${replyDto.depth}">
    <input type="hidden" name="groupId" value="${replyDto.groupId}">
	<table border="2" class = "table table-bordered">  
		<tr>
 			 <td>글쓴이 :</td>
 			 <td>${id}</td>
 		</tr>
 		<tr>	 
		 <td>제목 : </td>
		 <td><input style="width: 100%" type="text" name="title" value="[Re]"></td>			 
		</tr>
		<tr>
		  <td colspan="2">
		  <textarea style="width: 100%" rows="20" name="content" ></textarea>
		  </td>
	    </tr> 	    
	    <tr>
	      <td>첨부 : </td>
	      <td><input type="file"  name="fileData" multiple="multiple" ></td>
	    </tr>
	    <tr>
	      <td><input type="submit" value="글쓰기"></td>
	      <td><input type="reset" value="글쓰기취소">&nbsp<input type="button" value="목록으로" onclick="document.location.href='/bbs/list.bbs?pageNum=${pageNum}'"></td>	
	            	 
	    </tr>		
	</table>	
</form>
</center>
</div>
</body>
</html>