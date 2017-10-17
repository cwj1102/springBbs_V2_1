<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html>  
<head>
<meta charset="utf-8">
<title>게시판</title> 
<!-- <link rel="stylesheet" href="/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"> -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<style>
	#listTitle{
		font-size: 20px;
	}
	.write {
		float:right;
	}
	.write a{
		text-decoration: none;
	}
	.table tr{
 		transition:all 0.2s ease-in-out;
	}
	.table tr:hover:first-child{
		background-color : #fff;
		color : #000; 
	}
	.table tr:hover:last-child{
		background-color : #fff;
		color : #000; 
	}
	.table tr:hover:last-child a{
		color : #337ab7; 
	}
	.table tr:hover{
		background-color : #000;
		color : #fff;
	}
	.table tr:hover a{
		color : #fff;
	}
	.textleft{
	text-align: left;
	}
	h1{
    height: 143px;
    background: url(/bbs/resources/1499917576465.jpeg) no-repeat;
    background-size: 100% 143px;
}
h1 div{
    font-size: 65px;
    font-family: 'Droid Sans', sans-serif;
    font-weight: normal;
    text-transform: uppercase;
    color: #fff;
    float: right;
    position: relative;
    top:70px;
    right: 10px;
}
</style>
</head>

<body>
<div class="wrapper">
<div class="col-md-2 col-sm-1"></div>
<div class="col-md-8 col-sm-10"><h1 onclick="document.location.href='list.bbs?pageNum=1'"><div>자유게시판</div></h1></div>
<div class="col-md-2 col-sm-1"></div>
<div style="clear: both;"></div>
</div>
<div class="col-md-2 col-sm-1"><img src="/bbs/resources/ceo.jpg"></div>
<div class="col-md-8 col-sm-10">
<c:if test="${result!=null}">
	<script>
		alert($("{result}"));
	</script>
</c:if>
 <c:if test="${id!=null}">
 	<%@include file="loginOk.jsp" %>
 </c:if>
 
 <c:if test="${id==null}">
 	<%@include file="login.jsp" %>
 </c:if>
 <br/>
 <br/>
<center><b id="listTitle">글목록(전체 글:${totalCount})</b>
<table  class="write">
  <tr>
    <td align="right" >
       <a href="/bbs/write.bbs">글쓰기</a>
    </td>
  </tr>
</table>
<%-- <c:if test="${totalCount == 0}"> --%>
<!-- <table width="700" border="1" cellpadding="0" cellspacing="0"> -->
<!--   <tr> -->
<!--     <td align="center"> -->
<!--       게시판에 저장된 글이 없습니다. -->
<!--     </td> -->
<!--   </tr> -->
<!-- </table> -->
<%-- </c:if> --%>

<table class="table" width="700" cellpadding="2" cellspacing="2" align="center"> 
    <tr height="30" > 
      <td align="center"  width="50"  >번 호</td> 
      <td align="center"  width="250" >제   목</td> 
      <td align="center"  width="100" >작성자</td>
      <td align="center"  width="150" >작성일</td> 
      <td align="center"  width="50" >조 회</td>          
    </tr>

   <c:forEach var="article" items="${articleList}">
   <tr height="30">
    <td align="center"  width="50" >
	  <c:out value="${article.articleNum}"/>	   
	</td>
    <td class ="textleft" width="250" >  
      <c:if test="${article.depth > 0}">
	  	<img src="images/image3.png" width="${10 * article.depth}"  height="16">
	    <img src="images/cut.gif">
	  </c:if>
	  <c:if test="${article.depth == 0}">
	    <img src="images/image3.png" width="0"  height="16">
	  </c:if>         
      <a href="/bbs/content.bbs?articleNum=${article.articleNum}&pageNum=${pageNum}&fileStatus=${article.fileStatus}">
          ${article.title}</a> 
          <c:if test="${article.commentCount!=0}">
          	<a style="font-size: small;">[${article.commentCount}]</a>
          </c:if>
          <c:if test="${article.hit >= 20}">
            <img src="images/image3.png" border="0" height="16">
		  </c:if>
	</td>
    <td align="center"  width="100">${article.id}</td>
    <td align="center"  width="150">${article.writeDate}</td>
    <td align="center"  width="50">${article.hit}</td>
  </tr>
  </c:forEach>
  <tr>	  
      <td colspan="5" align="center" height="40">	 
	  ${pageCode}
	  </td>
  </tr>
</table>
</center>
</div>
<!-- <script src="/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script> -->
<!-- <script src="/webjars/jquery/3.2.1/dist/jquery.min.js"></script> -->

</body>
</html>