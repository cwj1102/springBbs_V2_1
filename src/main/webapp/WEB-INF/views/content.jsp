<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta charset="utf-8">
<title>글읽기</title>
<script src="https://code.jquery.com/jquery-3.2.0.min.js"></script>
<script>
$.ajaxSetup({
	type : "POST",
	async : true,
	dataType : "json",
	error : function(xhr){
		alert("error html = " + xhr.statusText);
	}
});

jQuery(function(){
	$("#commentWrite").on("click",function(){
		$.ajax({					
			url:"/bbs/commentWrite.comment",
// 			data{}에서는 EL을 ""로 감싸야함..그외에는 그냥 사용
			data:{				
				commentContent:$("#commentContent").val(),
				articleNum:"${article.articleNum}"
			},
			beforeSend : function(){
				/* alert("시작전"); */
			},
			complete: function(){
				/* alert("완료후"); */
			},
			success:function(data){
				/* if(data.result==1){ */
					alert("comment가 정상적으로 입력되었습니다");
					$("#commentContent").val("");
					getComment(1,event);
					/* showHtml(data,1); */
				/* } */
			}			
		}); 
	})
});

function getComment(commPageNum, event){
	//스크롤바 밑으로 내려가게 하기
	$.ajax({			
		url:"/bbs/commentRead.comment",	
		data:{
			articleNum:"${article.articleNum}",
// 			숫자와 문자연산에서 +를 제외하고는 숫자 우선
			commentRow:commPageNum*10
		},
// 			MappingJackson2JsonView를 이용할 때에는 Model에 심은 키값이 리턴되는 JSON데이터 앞에 키로 사용됨
		success:function(data){
// 			showHtml(data.commentList,commPageNum);
			showHtml(data,commPageNum);
		}				
	}); 	
}

function showHtml(data,commPageNum){	
	let html="<table class = 'table' align='center' >"/* style='undefined; table-layout: fixed; width: 100%; overflow:hidden;'>"; */
// 	let html+="<colgroup><col style='width: 132px;'><col style='width: 87px;'><col style='width: 67px;'><col style='width: 67px;'><col style='width: 81px;'></colgroup>";
	$.each(data, function(index,item){
		let localDate = new Date(item.commentDate);	
		let presentDay = item.commentDate;
		presentDay= presentDay.substring(0,10);
		html +="<tr>";
		html +="<td>"+(index+1)+"</td>";
		html +="<td>"+item.id+"</td>";
		html +="<td>"+item.commentContent+"</td>";
		html +="<td>"+presentDay+"</td>";
// 		html +="<td>"+item.commentDate+"</td>";
// 		html +="<td>"+localDate.toLocaleString()+"</td>";					
// 		html +="<td>"+localDate.toLocaleDateString()+"</td>";					
// 		html +="<td>"+localDate.toLocaleTimeString()+"</td>";					
// 		html +="<td>"+localDate.toDateString()+"</td>";					
// 		html +="<td>"+localDate.toTimeString()+"</td>";					
// 		html +="<td>"+localDate.toString()+"</td>";					
		html +="<td>"+item.articleNum+"</td>";					
		html +="</tr>";					
	});		
	html +="</table>";
	commPageNum=parseInt(commPageNum);
	if("${article.commentCount}">commPageNum*10){			
		nextPageNum=commPageNum+1;				
		html +="<br /><input type='button' onclick='getComment(nextPageNum,event)' value='다음comment보기'><br>";
	}
	$("#showComment").html(html);	
	$("#commentContent").val("");
	$("#commentContent").focus();
}
</script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<style>
    #comment{
        width: 300px;
        overflow: hidden;
    }
    </style>
</head>
<body> 
<div class="col-md-3 col-sm-2"></div>
<div class="col-md-6 col-sm-8">
${pageNum }<br/>
뎁스 : ${article.depth }<br/>
그룹아이디 : ${article.groupId }<br/>  
   <form action="/bbs/replyForm.bbs" method="post">      
    <input type="hidden" name="pageNum" value="${pageNum}">
    <input type="hidden" name="articleNum" value="${article.articleNum}">                 
    <input type="hidden" name="depth" value="${article.depth}">
<%--     <input type="hidden" name="groupId" value="${article.groupId}"> --%>
	<table class = "table table-bordered" border="1" width="500" align="center">  
		<tr>
 			 <td width="40">글쓴이</td> <td width="100">${article.id}</td> 			 
 			 <td width="40">조회수</td> <td width="100">${article.hit}</td>
 		</tr>
 		<tr>	 
			 <td>제목</td><td>${article.title}</td>
			 <td>날짜</td><td>${article.writeDate}</td>
		 </tr>	
		 <tr>
			<td colspan="2">다운로드 </td>
			<td colspan="2">
<%-- 			<a href="/bbs/download.bbs?fname=${article.fileStatus}">${article.fileStatus}</a></td> --%>
   			<c:forEach var="uploadList" items="${uploadList}">
	  			<a href="/bbs/download.bbs?storedFname=${uploadList.storedFname}" target="_blank"><c:out value="${uploadList.originFname}"/><br/></a>
	  		</c:forEach>
		 </tr>					
		 <tr>			 
			  <td colspan="4" ><xmp>${article.content}</xmp></td>
	     </tr> 	
	          
	     <tr>
	      <c:if test="${id !=null}">
	    	  <td colspan="4" align="right">	    	
	    	  <input type="submit" value="답글달기">
	    	  <c:if test="${id ==article.id}">
	    	  <input type="button" value="수정하기" onclick="document.location.href='/bbs/update.bbs?articleNum=${article.articleNum}&pageNum=${pageNum}'">
	    	  <input type="button" value="삭제하기" onclick="document.location.href='/bbs/delete.bbs?articleNum=${article.articleNum}&pageNum=${pageNum}'">
	    	  </c:if>
	    	  <c:if test="${id !=article.id}">
	    	  <input type="button" value="수정하기" disabled="disabled">
	    	  <input type="button" value="삭제하기" disabled="disabled">
	    	  </c:if>
	    	  <input type="button" value="목록으로" onclick="document.location.href='/bbs/list.bbs?pageNum=${pageNum}'">
	    	  </td>
	      </c:if>
	      		    	
	      <c:if test="${id ==null}">
	    	  <td colspan="4" align="right">
	    	  <input type="submit" value="답글달기" disabled="disabled">
	    	  <input type="button" value="수정하기" disabled="disabled">
	    	  <input type="button" value="삭제하기" disabled="disabled">
	    	  <input type="button" value="목록으로" onclick="document.location.href='/bbs/list.bbs?pageNum=${pageNum}'">
	    	  </td>   
	      </c:if>      	 	      	 
	     </tr>
	     <tr>
		     <td colspan="4">
		   	   <textarea rows="5" cols="70" id="commentContent"></textarea><br /><br />
			   <c:if test="${id ==null}">
		    	  <input type="button" value="comment 쓰기" disabled="disabled">    	  
		       </c:if> 
		       <c:if test="${id !=null}">
	    	 	 <input type="button" value="comment 쓰기" id="commentWrite">
	     	   </c:if>	     	  
	     	   <input type="button" value="comment 읽기(${article.commentCount })" 
	     	       onclick="getComment(1,event)" id="commentRead">	     	       
		   </td> 
		 </tr> 		
	 </table>		 	
	</form>
	<form>
	
	
	<div>
		<div id="showComment" align="center">
		</div>
		<input type="hidden" id="commPageNum" value="1">
	</div>
	</div>	
	</form>


</body>
</html>