<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>회원 정보 수정</title>
	</head>	
	<body>
		<h1>회원 정보 수정</h1>
		
		<!-- form:post>(label+input)*3+button -->
		<form action="/member/update" method="post">
			<input type="hidden" name="id" value="${ member.id }">

		    <label for="name">이름: </label>
		    <input type="text" id="name" name="name" value="${ member.name }" required> <br>
		    
		    <label for="email">이메일: </label>
		    <input type="text" id="email" name="email" value="${ member.email }" required> <br>
		    
		    <label for="age">나이: </label>
		    <input type="number" id="age" name="age" value="${ member.age }"> <br>
		    
		    <button>수정</button>
		</form>

		<p>
		    <a href="/member/list">목록으로 돌아가기..</a>
		</p>		
	</body>
</html>