<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"/>
	<h2 class="page-title">게시판</h2>
	
	<!-- 글쓰기 버튼을 추가 -->
	<h4 class="text-right">
		<a class="btn btn-outline" href="/board/write">글쓰기</a>
	</h4>
	
	<!-- 검색 영역 -->
	<form class="search-bar" action="/board/list" method="get">
	    <select name="category" class="search-bar__select">
	        <option value="전체" ${condition.category == '전체' ? 'selected' : ''}>전체</option>
	        <option value="자유" ${condition.category == '자유' ? 'selected' : ''}>자유</option>
	        <option value="질문" ${condition.category == '질문' ? 'selected' : ''}>질문</option>
	        <option value="공지" ${condition.category == '공지' ? 'selected' : ''}>공지</option>
	    </select>
	    <select name="searchType" class="search-bar__select">
	        <option value="titleContent" ${condition.searchType == 'titleContent' ? 'selected' : ''}>제목+내용</option>
	        <option value="title" ${condition.searchType == 'title' ? 'selected' : ''}>제목</option>
	        <option value="content" ${condition.searchType == 'content' ? 'selected' : ''}>내용</option>
	        <option value="writer" ${condition.searchType == 'writer' ? 'selected' : ''}>작성자</option>
	    </select>
	    <input type="text" class="search-bar__input" name="keyword" value="${condition.keyword}" placeholder="검색어를 입력하세요.">
	    <button type="submit" class="btn btn-primary">검색</button>
	</form>	
	
	
	<c:choose>
		<c:when test="${empty boardList}">
			<p>등록된 게시글이 없습니다.</p>
		</c:when>
		<c:otherwise>
			<div class="board-table-wrap">
				<table class="board-table">
					<thead>
					    <tr>
					        <th class="board-table_col-no">번호</th>
					        <th class="board-table_col-category">카테고리</th>
					        <th class="board-table_col-title">제목</th>
					        <th class="board-table_col-writer">작성자</th>
					        <th class="board-table_col-date">작성일</th>
					        <th class="board-table_col-count">조회수</th>
					    </tr>
					 </thead>
					 <tbody>
						<c:forEach var="board" items="${boardList}" varStatus="status">
							<tr onclick="location.href = '/board/detail/${board.boardId}'">
								<td class="board-table_col-no">
									${pageInfo.totalCount - (pageInfo.page -1) * pageInfo.size - status.index}
								</td>
								<td class="board-table_col-category">
									<span class="board-table_category">${board.category}</span>
								</td>
								<td class="board-table_col-title">${board.title}</td>
								<td class="board-table_col-writer">${board.writerNickname}</td>
								<td class="board-table_col-date">${board.createAtStr}</td>
								<td class="board-table_col-count">${board.count}</td>
							</tr>
						</c:forEach>
					 </tbody>
				</table>
			</div>
		</c:otherwise>	
	</c:choose>
	
	<%-- 페이징 바 영역 --%>
	<nav class="pagenation">
		<%-- 이전 페이지 그룹이 있을 경우 표시 --%>
		<c:if test="${pageInfo.hasPrevGroup}">
	        <a class="pagenation-item"
	           href="/board/list?page=${pageInfo.startPage - 1}&searchType=${condition.searchType}&category=${condition.category}&keyword=${condition.keyword}"
	        ><<</a>
		</c:if>
    	<%-- ------- --%>
		
		<%-- 현재 페이지 그룹 만큼 표시 --%>
		<c:forEach var="p" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
		
	        <a class="pagenation-item ${p == pageInfo.page ? 'pagenation-item_active' : ''}"
	           href="/board/list?page=${p}&searchType=${condition.searchType}&category=${condition.category}&keyword=${condition.keyword}"
	        >${p}</a>
		
		</c:forEach>
		<%-- -------- --%>
		
		<%-- 다음 페이지 그룹이 있을 경우 표시 --%>
		<c:if test="${pageInfo.hasNextGroup}">
	        <a class="pagenation-item"
	           href="/board/list?page=${pageInfo.endPage + 1}&searchType=${condition.searchType}&category=${condition.category}&keyword=${condition.keyword}"
	        >>></a>
		</c:if>
    	<%-- -------- --%>
	</nav>	

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>





