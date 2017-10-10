<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>로그인 하세요..^^</title>
<style type="text/css">
	.right{
		float:right;
	}
	.login{
		height: 70px;
		width: 100%;
	}
	.join{
		width:100%;
		}
		.clear{
		clear: both;
		}	
	table{
	text-align: center;
	}
/* 	.center{ */
/* 		display: table; margin-left: auto; margin-right: auto; */
/* 	} */
/* 	.center form{ */
/* 	display: block; */
/* 	margin: 0 auto; */
/* 	} */
</style>
	<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

</head>
<body>
	<div class="right">
<!-- 		<div class="center"> -->
	
	<form action="/bbs/login.bbs" method="post" id="loginForm">
		<input type="hidden" name="pageNum" value="${pageNum}">
		
		<table>
		<tr><td> I D  </td><td colspan="2"><input type="text" class="form-control" name="id" id="id"></td><td rowspan="2"><input class="btn btn-default login" type="submit" value="로그인"></td></tr>
		<tr><td>PASS  </td><td colspan="2"><input type="password" class="form-control" name="pass" id="pass"></td></tr> 
		<tr><td></td><td></td><td></td><td> 
		<!-- <input class="btn btn-default btn-xs" type="reset" value="취소"> --><input class="btn btn-default btn-sm center join" type="button" value="회원가입" onclick="document.location.href='/bbs/joinForm.bbs'"></td></tr>
		</table>
		
		<!-- <label for="id"> I D : <input type="text" name="id" id="id"></label><br />
		<label for="pass">PASS : <input type="text" name="pass" id="pass"></label><br/><br/> 
		<input type="submit" value="로그인"> 
		<input type="reset" value="취소"> 
		<input type="button" value="회원가입" onclick="document.location.href='/bbs/joinForm.bbs'"> -->
	</form>
	</div>
	<div class="clear"></div>
</body>
</html>