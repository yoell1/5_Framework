<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<section class="hero">
	<h1 class="hero-title">커뮤니티에 오신것을 환영합니다.</h1>
	<p class="hero-desc">자유롭게 글을 쓰고 이야기를 나눠보세요.</p>
	<div class="hero-actions">
		<a class="btn" href="/board/list">게시글 둘러보기</a>
		<c:if test="${empty sessionScope.loginMember}">
			<a class="btn" href="/member/login">로그인</a>
		</c:if>
	</div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>