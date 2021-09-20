package net.scit.board.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.scit.board.vo.Reservation;

public interface ReservationMapper {

	public int insertReservation(Reservation rv) throws Exception;

	public int timeCheck(@Param("reservation_time")String reservation_time, @Param("reservation_date")String reservation_date) throws Exception;

	public int timeCheck(Reservation rv) throws Exception;


}
