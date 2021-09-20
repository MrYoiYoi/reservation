<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>에약 조회</title>

	
<script>
	function detailBoard(num) {
		let targetPlace = 'detailboard?boardnum=' + num;
		location.href = targetPlace;
	}
</script>
</head>
<body>
<div class="wrapper">
	<h2>[ 게시판 ]</h2>
	<div class="home">
		<p><a href="${pageContext.request.contextPath}/"><img src="resources/images/home.png"></a></p>
		<form id="search" action="listboard" method="GET">
			<select name="searchItem">
				<option value="title"  ${searchItem=='title' ? 'selected' : ''}>제목</option>
				<option value="userid" ${searchItem=='userid'? 'selected' : ''}>작성자</option>
				<option value="text"   ${searchItem=='text'	 ? 'selected' : ''}>글내용</option>
			</select>
			<input type="text" name="searchWord" value="${searchWord}">
			<input class="btn" type="submit" value="검색">
		</form>
	</div>
	
	<!-- 게시글 목록 시작 -->
	<table border="1">
		<tr>
			<th>번호</th>
			<th class="title">글제목</th>			
			<th>글쓴날</th>			
			<th>작성자</th>			
			<th>조회수</th>			
		</tr>
		
		<!-- 게시글 출력 -->
		<c:forEach var="board" items="${list}" varStatus="stat">		
			<tr>
				<td>${board.boardnum} / ${stat.count}</td>
				<td class="title">
					<c:if test="${board.originalfile != null}">
					<img src="resources/images/attach.png" style= "width:13px;">
					</c:if>
					<a href="javascript:detailBoard(${board.boardnum});">${board.title}</a>
				</td>
				<td>${board.regdate}</td>
				<td>${board.userid}</td>
				<td>${board.hitcount}</td>
			</tr>
		</c:forEach>
	</table>
	
	<!--  글쓰기 버튼 -->
	<c:if test="${sessionScope.loginId != null}">
		<div class="write"><a href="writeboard">글쓰기</a></div>
	</c:if>
	
	<p>글 개수 : ${totalRecordCount}</p>
	<!-- 페이징 출력되는 부분 -->
	<p>현재 요청한 페이지 : ${currentPage}</p>
	<div class="navigator">
		◀ &nbsp;
		<c:forEach var="page" begin="1" end="${totalPageCount}">
			<c:if test="${currentPage==page}">
				<span style="color:blue; font-weight:bolder; font-size:1.3em">${page}</span>&nbsp;
			</c:if>
			<c:if test="${currentPage !=page}">
				<a href="listboard?currentPage=${page}&searchItem=${searchIte}&searchWord=${searchWord}" >${page}</a> &nbsp;
			</c:if>
		</c:forEach>
		▶ &nbsp;
	</div>
</div>
</body>
</html>
