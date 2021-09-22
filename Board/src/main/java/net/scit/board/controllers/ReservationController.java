package net.scit.board.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import net.scit.board.dao.ReservationRepository;
import net.scit.board.vo.Member;
import net.scit.board.vo.Reservation;


@Controller
public class ReservationController {
	private static Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	ReservationRepository repository;
	
	@RequestMapping("/listReservation")
	public String listReservation(Model model, HttpSession session) {
		
//		String loginId = (String)session.getAttribute("loginId");
		
		Object object = session.getAttribute("loginId");
	      String userid = (String) object;
	      logger.info("member : {}", userid);
		
		List<Reservation> list = repository.selectReservation(userid);
		System.out.println(list);
		
		
		model.addAttribute("list", list);	
		
		return "board/listReservation";
	}
	
	
	
	
	/**
	 * 예약 등록을 위한 요청
	 * @param session
	 * @param rv
	 * @return
	 */
	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public String reservation(HttpSession session, Reservation rv, Model model) {
		
		Object object = session.getAttribute("loginId");
		String userid = (String) object;
		logger.info("member : {}", userid);
		
		Object object2 = session.getAttribute("loginName");
		String username = (String) object2;
		logger.info("member : {}", username);
		
		int result = repository.timeCheck(rv);
		String message = "";
		if (result >=1) {
			message = "예약을 할 수 없습니다.";
			model.addAttribute("message", message);
			return "board/reservation";
		}
		else {
		repository.insert(rv);
		}
		logger.info("게시글 등록 여부 {} : " + rv);
		//int result = repository.insert(board);
		//logger.info("게시글 등록 여부 : {}", result);
		
		
		return "index";
	}
	
	/**
	 * 예약시간과 날짜 중복 확인
	 * @param rv
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "timeCheck", method = RequestMethod.POST)
	public int timeCheck(Reservation rv){
		
		logger.info("timeCheck() 진입");
		
		
		int result = repository.timeCheck(rv);
		
		logger.info("결과값 = " + result);
		
		//result = 1 >> 중복
		//result = 0 >> 비어있음
		
		return result;
		
		
//	@RequestMapping(value = "timeCheck", method = RequestMethod.POST)
//	@ResponseBody
//	public String timeCheck(String reservation_time, String reservation_date){
//		
//		logger.info("timeCheck() 진입");
//		
//		
//		int result = repository.timeCheck(reservation_time,reservation_date);
//		
//		logger.info("결과값 = " + result);
//		return "fuck";
//		if(result != 0) {
//			
//			return "fail";	// 중복 아이디가 존재
//			
//		} else {
//			
//			return "success";	// 중복 아이디 x
//			
//		}	 
	} // memberIdChkPOST() 종료	
	
	
//	@RequestMapping(value = "idchek", method = RequestMethod.POST)
//	@ResponseBody
//	public String idchek(String userid){
//		
//		//logger.info("memberIdChk() 진입");
//		
//		
//		int result = service.idCheck(userid);
//		
//		logger.info("결과값 = " + result);
//		
//		if(result != 0) {
//			
//			return "fail";	// 중복 아이디가 존재
//			
//		} else {
//			
//			return "success";	// 중복 아이디 x
//			
//		}	 
//	} // memberIdChkPOST() 종료	

//	@RequestMapping(value = "/reservation1", method = RequestMethod.POST)
//	public String reservation(ReservationDTO dto, HttpSession session, Model model) throws ParseException {
//		dto.setM_id((String) session.getAttribute("m_id"));
//		int duplicateFind = service.DuplicateFind(dto);
//		if (duplicateFind >= 1) {
//			model.addAttribute("duplicateFind", duplicateFind);
//			return "/reservation/ReservationCheck";
//		}
//
//		String ReservationNumber = UUID.randomUUID().toString(); // UUID 생성 예약번호로 사용할 것
//		dto.setReservation_number(ReservationNumber);
//		service.reservation_number_people(dto);
//		int RoomPrice = service.RoomPrice(dto);
//		// 체크인 체크아웃 날짜간 차이를 구한다
////		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 내가 원하는 날짜 형식을 지정 (포맷)
////		Date beginDate = formatter.parse(dto.getReservation_data_in()); // 체크인
////		Date endDate = formatter.parse(dto.getReservation_data_out()); // 체크아웃
////		long diff = endDate.getTime() - beginDate.getTime(); // 체크아웃 - 체크인 시간차이 兮
////
////		long diffDays = diff / (24 * 60 * 60 * 1000) + 1;// 날짜 계산 하루예약은 0이므로 +1해준다.
////		// 날짜 차이 * 룸 가격
////		int price = RoomPrice * (int) diffDays;
////		dto.setPrice(price);
//		service.reservationInsert(dto);
//		model.addAttribute("dto", dto);
//		return "/reservation/ReservationCheck";
//	}
//
//
//
}
