<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.css">
<script src="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script>

<script>

/**
 * 이름체크, 시간 중복확인
 */
function nameCheck() {
	
	let username = document.getElementById("username");
	
	
	if(username.value.trim().length == 0) {
		alert("이름을 입력해주세요");
		username.select();
		return false;
		}
	
	
	}

</script>

</head>
<body>

<div class="container">
	<h2>[ 병원 예약 ]</h2>
	<form action="reservation" method="POST">
		<!-- <input type="datetime-local" step="1000" value="날짜" id="reservation_date" class="reservation_date"> -->
		
		<table>
			<tr>
				<td>날짜</td>
				<td>
					<input type="date" name="reservation_date"id="datefield" min="2021-07-20" max="2021-10-10" required>
				</td>
			</tr>
			<tr>
				<td>시간</td>
				<td>
					<input type="text" id="time1" name="reservation_time" class="form-control" style="width:200px;">
					<span id="time_input_re"></span>
				</td>
			</tr>				
			<tr>
				<td>이름</td>
				<td>
					<input type="text" name="username" id="username">
				</td>
			</tr>
		</table>
		
		<input type="submit" class="btn btn-info" value="예약하기" onclick="return nameCheck();">
		
<!-- 시간 정해놓기 (9시부터 17시까지) 점심시간은 아직 안됨 -->		
	<script>
	
		
	$(function() {
		
    $("#time1").timepicker({
        timeFormat: 'h:mm p',
        interval: 60,
        minTime: '9',
        maxTime: '17',
        defaultTime: '11',
        startTime: '9',
        dynamic: false,
        dropdown: true,
        scrollbar: true,
        change: time1
    });
  	  	function time1 () {
			
			//alert('gg');
	
  	  	var time = $('#time1').val(); //time값이 "time1"인 입력란의 값을 저장
  	  	var date = $('#datefield').val();
  		var allData = {reservation_time : time, reservation_date : date}
  	    $.ajax({
  	        url:'timeCheck', //Controller에서 인식할 주소
  	        type:'post', 
  	        data: allData,
  	     	dataType : "json",
  	     	//cache: false,
  	    	async : false,
  	        success: function(data) {
  	        	//alert(data);
  	        	//console.log("성공 여부" + result);
  	        	
  	        		if(data != 1){
  						$('#time_input_re').css("color", "blue");
  						$("#time_input_re").text("예약 가능합니다");
  						
  					} else {
  						$("#time_input_re").css("color", "red");
  						$("#time_input_re").text("예약이 불가능합니다.");	
  						
  					}
  	        	
  	       		}
	  		


  	   		 });
			
			
		};
   });
	
		</script>
		
	</form>
<!-- 날짜 오늘부터 7일후까지-->			
	<script>
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0 so need to add 1 to make it 1!
	var yyyy = today.getFullYear();
	if(dd<10){
	  dd='0'+dd
	} 
	if(mm<10){
	  mm='0'+mm
	} 
	today2=today
	today = yyyy+'-'+mm+'-'+dd;
	document.getElementById("datefield").setAttribute("min", today);
	today2=yyyy+'-'+mm+'-'+(dd+7);
	document.getElementById("datefield").setAttribute("max", today2);
	</script>	

</div>
</body>
</html>
