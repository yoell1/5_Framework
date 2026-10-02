<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

	<h2 class="page-title">마이페이지</h2>
	
	<div class="mypage-card">
		
		<c:choose>
			<c:when test="${not empty loginMember.profile}">
				<img class="profile-preview" src="${loginMember.profile}" alt="프로필 이미지" />
			</c:when>			
			<c:otherwise>
				<div class="profile-preview profile-preview-placeholder">사진없음</div>
			</c:otherwise>
		</c:choose>
		
		<%--
			* <dl> : 이름-값 형태로 정보를 묶어주는 전체 목록 
			* <dt> : 정보의 제목/이름
			* <dd> : 제목에 대한 상세 값/내용
		--%>
		<dl class="mypage-info">
			<dt>아이디</dt>
			<dd>${loginMember.memberId}</dd>
			
			<dt>이름</dt>
			<dd>${loginMember.memberName}</dd>
			
			<dt>닉네임</dt>
			<dd>${loginMember.nickname}</dd>
			
			<dt>이메일</dt>
			<dd>${loginMember.email}</dd>
			
			<dt>가입일</dt>
			<dd>${loginMember.createAtStr}</dd>
		</dl>
		
		
	</div>
	
	<form action="/member/withdraw" method="post" class="form form-flex form-row-center"
	      onsubmit="return confirm('정말 탈퇴하시겠습니까?')">
		<button class="btn btn-danger">회원 탈퇴</button>
	</form>

	
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>











