<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

	<h2 class="page-title">회원가입</h2>

	<c:if test="${ error != null }">
		<p class="alert alert-error">
			${ error }
		</p>
	</c:if>	

	<%--
		* enctype="multipart/form-data"
		  : 전송 데이터(요청 파라미터)에 파일이 있을 경우 설정하는 옵션.
		    이 속성이 없으면 파일의 내용이 전달되지 않음!
			파일의 실제 바이너리 데이터를 함께 보내기 위한 필수 설정		
	--%>
    <form id="join-form" class="form form-flex" 
		  action="/member/join" method="post" enctype="multipart/form-data">
        <div class="form-row form-row-center">
            <div class="profile-preview-wrap">
                <div id="profile-preview-placeholder" class="profile-preview profile-preview-placeholder">사진없음</div>
                <img id="profile-preview" class="profile-preview" alt="프로필 미리보기" style="display:none;">
            </div>
            <label class="file-label">
                프로필 이미지 선택
                <input type="file" id="profile-image" name="profileImage" accept="image/*">
            </label>
        </div>

        <div class="form-row">
            <label for="member-id">아이디</label>
            <div class="input-with-button">
                <input type="text" id="member-id" name="memberId" required autocomplete="off">
                <button type="button" id="check-id-btn" class="btn btn-outline">중복확인</button>
            </div>
            <p id="check-id-result" class="form-tip"></p>
        </div>

        <div class="form-row">
            <label for="member-pwd">비밀번호</label>
            <input type="password" id="member-pwd" name="memberPwd" required>
        </div>

        <div class="form-row">
            <label for="member-pwd">비밀번호 확인</label>
            <input type="password" id="member-pwd-confirm" required>
            <p id="check-pwd-result" class="form-tip"></p>
        </div>

        <div class="form-row">
            <label for="member-name">이름</label>
            <input type="text" id="member-name" name="memberName" required>
        </div>

        <div class="form-row">
            <label for="nickname">닉네임</label>
            <input type="text" id="nickname" name="nickname" required>
        </div>

        <div class="form-row">
            <label for="email">이메일</label>
            <input type="text" id="email" name="email" autocomplete="off">
        </div>

        <div class="form-row">
            <button type="submit" class="btn btn-primary">가입하기</button>
        </div>
    </form>
<script src="/js/member.js"></script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>








