package net.scit.board.vo;

import lombok.Data;

@Data
public class Reservation {
	
	public int reservation_num;
	public String userid;
	public String username;
	public String reservation_date;
	public String treatment_kind;
	public String reservation_time;
	
}
//CREATE TABLE reservation
//(
//	reservation_num number primary key,	-- 예약번호
//	username   varchar2(50) not null,	-- 아이디
//	reservation_date date not null,	-- 예약날짜
//	treatment_kind varchar(20) default '검진'	,	-- 진료종류
//	reservation_time varchar(50), -- 예약시간
//	userid varchar2(50) not null references members(userid)
//);