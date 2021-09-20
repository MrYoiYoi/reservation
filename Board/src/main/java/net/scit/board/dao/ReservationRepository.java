package net.scit.board.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.scit.board.vo.Reservation;

@Repository
public class ReservationRepository {

	@Autowired
	SqlSession session;

	public int insert(Reservation rv) {
		ReservationMapper mapper = session.getMapper(ReservationMapper.class);
		
		int result = 0;
		
		try {
			result = mapper.insertReservation(rv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public int timeCheck(String reservation_time, String reservation_date) {
		ReservationMapper mapper = session.getMapper(ReservationMapper.class);
		int result = 0;
		
		try {
			result = mapper.timeCheck(reservation_time,reservation_date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public int timeCheck(Reservation rv) {
		ReservationMapper mapper = session.getMapper(ReservationMapper.class);
		int result = 0;
		
		try {
			result = mapper.timeCheck(rv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	
}
