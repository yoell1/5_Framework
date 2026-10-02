<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
    <h2 class="page-title">게시글 상세보기</h2>
	
	<input type="hidden" id="board-key" value="${board.boardId}">
	<article class="board-detail">
        <header>
            <span>${board.category}</span>
            <h2>${board.title}</h2>
            <div>
                <span>${board.writerNickname}</span>
                <span>${board.createAtStr}</span>
                <span>조회 ${board.count}</span>
            </div>
        </header>

        <div class="board-detail_content">${board.content}</div>

		<%-- 이미지가 있을 경우 표시 --%>
		<c:if test="${not empty board.images}">
			<ul class="board-image-list">
				<%-- 이미지 개수 만큼 표시 --%>
				<c:forEach var="img" items="${board.images}">
					<li><img src="${img.imagePath}" alt="${img.originalName}"> </li>
				</c:forEach>			
			</ul>
		</c:if>
		
		<%-- 작성자인 경우 표시 --%>
		<c:if test="${isOwner}">
			<div class="board-detail_actions">
				<form action="/board/delete/${board.boardId}" method="post"
					onsubmit="return confirm('게시글을 삭제하시겠습니까?')">
					<button type="submit" class="btn btn-danger">삭제</button>
				</form>
			</div>
		</c:if>

    </article>	

	<%-- 댓글 영역 --%>
    <section class="comment-section">
        <h3 class="comment-section_title">댓글 ${empty comments ? 0 : comments.size()}</h3>
        <ul class="comment-list" id="comment-list">
            <c:forEach var="comment" items="${comments}">
                <li id="comment-${comment.commentId}">
                    <div class="comment-list_body">
                        <span class="comment-list_writer">${comment.writerNickname}</span>
                        <span class="comment-list_content">${comment.content}</span>
                        <span class="comment-list_date">${comment.createAtStr}</span>
                    </div>
                    <c:if test="${not empty loginMember and loginMember.memberId == comment.memberId}">
                        <button type="button" class="btn btn-outline comment-delete-btn" data-comment-id="${comment.commentId}">삭제</button>
                    </c:if>
                </li>
            </c:forEach>
        </ul>

        <c:choose>
            <c:when test="${not empty loginMember}">
                <form class="comment-form" id="comment-form">
                    <textarea placeholder="댓글입력..." name="content" rows="2" required></textarea>
                    <button type="submit" class="btn btn-primary">등록</button>
                </form>
            </c:when>
            <c:otherwise>
                <p class="form-tip"><a href="/member/login">로그인</a> 후 댓글을 작성하세요.</p>
            </c:otherwise>
        </c:choose>
    </section>

    <!--
		댓글 목록을 표시하는 영역에서 사용할 템플릿으로 임시 저장한 UI (브라우저에서 해석되지 않음, 마크업 구조 보관용)
    -->
    <template id="comment-template">
        <li>
            <div class="comment-list_body">
                <span class="comment-list_writer"></span>
                <span class="comment-list_content"></span>
                <span class="comment-list_date"></span>
            </div>
            <button type="button" class="btn btn-outline comment-delete-btn">삭제</button>
        </li>
    </template>
<script src="/js/board.js"></script>	
<jsp:include page="/WEB-INF/views/common/footer.jsp" />






