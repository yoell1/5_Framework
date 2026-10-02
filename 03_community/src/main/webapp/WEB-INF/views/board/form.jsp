<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
    <h2 class="page-title">게시글 작성</h2>
    
    <form action="/board/write" method="post" enctype="multipart/form-data"
          class="form form-flex">
          <div class="form-row">
              <label for="category">카테고리</label>
              <select id="category" name="category">
                  <option value="자유">자유</option>
                  <option value="질문">질문</option>
                  <option value="공지">공지</option>
              </select>
          </div>
          
          <div class="form-row">
              <label for="title">제목</label>
              <input type="text" id="title" name="title" required>
          </div>

          <div class="form-row">
              <label for="content">내용</label>
              <textarea id="content" name="content" rows="10" required></textarea>
          </div>    
          
          <div class="form-row">
              <label for="images">첨부 이미지(여러장 가능)</label>
              <%--
                  multiple -> 사용자가 파일 선택창에서 여러장을 한번에 고를 수 있다.
              --%>
              <input type="file" id="images" name="imageFiles" accept="image/*" multiple>
              <div id="image-preview-list" class="board-image-list"></div>
          </div>

          <div class="form-row">
              <button type="submit" class="btn btn-primary">등록</button>
          </div>                        
    </form>
<script src="/js/board.js"></script>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />