<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>회원 관리</title>
	</head>
	<body>
		<h1>회원 목록</h1>
		
		<p>
			<a href="/insertForm.html">회원 등록</a>
		</p>
		
		<table border="1">
			<thead>
				<tr>
					<th>No</th>
					<th>회원번호</th>
					<th>이름</th>
					<th>이메일</th>
					<th>나이</th>
					<th>삭제</th>
					<th>수정</th>
				</tr>
			</thead>
			<tbody>
				<%-- JSTL 사용 --%>
				<%-- 조건문(c:if)을 사용하여 리스트가 비어있는 경우, (test 속성)
						"등록된 회원이 없습니다" 출력 --%>
				<c:if test="${ empty memberList }">
					<tr>
						<td colspan="7">등록된 회원이 없습니다.</td>
					</tr>
				</c:if>
						
				<%-- 반복문(c:forEach)을 사용하여 조회된 결과 개수만큼, (items 속성)
						한 행씩 데이터를 출력 --%>
				<c:forEach var="m" items="${ memberList }" varStatus="status" >
					<tr>	
						<td>${ status.count }</td>
						<td>${ m.id }</td>
						<td>${ m.name }</td>
						<td>${ m.email }</td>
						<td>${ m.age }</td>
						<td>
							<a href="/member/delete/${ m.id }">삭제</a>
						</td>
						<td>
							<a href="/member/update/${ m.id }">수정</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		
	</body>
</html>