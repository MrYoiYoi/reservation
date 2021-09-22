<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>에약 조회</title>

	

</head>
<body>
<div class="wrapper">
	<h2>[ 예약 내역 ]</h2>
	
	<!-- 게시글 목록 시작 -->
	<table border="1">
		<tr>
			<th>이름</th>				
			<th>진료종류</th>
			<th>예약날짜</th>					
			<th>예약시간</th>			
		</tr>
		
		<!-- 게시글 출력 -->
		<c:forEach var="board" items="${list}" varStatus="stat">		
			<tr>
				
				<td>
					${board.username}
				</td>
				<td>${board.treatment_kind}</td>
				<td>${board.reservation_date}</td>
				<td>${board.reservation_time}</td>
			</tr>
		</c:forEach>
	</table>
</div>
</body>
</html>

