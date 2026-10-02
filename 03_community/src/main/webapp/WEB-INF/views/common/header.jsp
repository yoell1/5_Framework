<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>커뮤니티</title>
		
		<link rel="stylesheet" href="/css/common.css">
		<link rel="stylesheet" href="/css/board.css">
	</head>
	<body>
		<header class="site-header">
			<div class="site-header-inner">
				<a href="/" class="logo">커뮤니티</a>
				
				<nav class="nav">
					<c:choose>
						<c:when test="${ not empty sessionScope.loginMember }">
							<span>${sessionScope.loginMember.nickname}님</span>
							
							<a href="/board/list">게시판</a>
							<a href="/member/mypage">마이페이지</a>
							<a href="/member/logout">로그아웃</a>
						</c:when>
						<c:otherwise>
							<a href="/member/login">로그인</a>
							<a href="/member/join">회원가입</a>				
						</c:otherwise>		
					</c:choose>
					
				</nav>
			</div>			
		</header>
		<main class="container">
			
			
			
			
			