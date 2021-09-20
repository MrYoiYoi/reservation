package net.scit.board.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.scit.board.dao.BoardRepository;
import net.scit.board.util.FileService;
import net.scit.board.util.PageNavigator;
import net.scit.board.vo.Board;

@Controller
public class BoardController {
	private static Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	BoardRepository repository;
	
	final String uploadPath = "/boardfile"; // 파일이 저장될 경로
	
	@RequestMapping("/listboard")
	public String listboard(
			@RequestParam(value="currentPage", defaultValue="1") int currentPage,
			@RequestParam(value="searchItem",defaultValue="title") String searchItem, 
			@RequestParam(value="searchWord",defaultValue="")  String searchWord,
			Model model) {
		
		logger.info("searchItem : {}", searchItem);
		logger.info("searchWord : {}", searchWord);
		logger.info("요청한페이지 " + currentPage);
		
		
		
		// Paging 관련
		int countPerPage = 10;
		
		int totalRecordCount = repository.getBoardCount(searchItem, searchWord);
		logger.info("글 전체 개수 : " + totalRecordCount);
		
		
		//PageNavigator navi = new PageNavigator(currentPage,totalRecordCount);
		
		
		
		int totalPageCount = (totalRecordCount % countPerPage > 0) 
			    ? totalRecordCount / countPerPage + 1 : totalRecordCount / countPerPage;
		
		//3 : s 21 e: 30
		int srow = countPerPage * currentPage - 9;
		int erow = countPerPage * currentPage;
		
		logger.info("페이지수 : " + totalPageCount); // 3 ( ◀ 1 2 3 ▶ )
		// 1) DB에 접속해서 글내용을 전부 가져옴
		List<Board> list = repository.selectAll(srow, erow, searchItem, searchWord);
		
		// 2) 글내용을 Model에 넣는 작업 수행
		model.addAttribute("list", list);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("totalRecordCount", totalRecordCount);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("currentPage", currentPage);
		return "board/listBoard";
	}
	
	/**
	 * 글 등록을 위한 화면 요청
	 * @return
	 */
	@RequestMapping("/writeboard")
	public String writeboard() {
		
		return "board/writeBoard";
	}
	
	/**
	 * 글 등록 처리를 위한 요청
	 * @return
	 */
	@RequestMapping(value="/writeboard", method=RequestMethod.POST)
	public String writeboard(Board board, MultipartFile upload) {
		if(!upload.isEmpty()) {
			String originalFilename = upload.getOriginalFilename();
			String savedFilename = FileService.saveFile(upload, uploadPath);
			
			board.setOriginalfile(originalFilename);
			board.setSavedfile(savedFilename);
		}
		System.out.println(board);
		
		int result = repository.insert(board);
		logger.info("게시글 등록 여부 {} : " + board);
		//int result = repository.insert(board);
		//logger.info("게시글 등록 여부 : {}", result);
		
		return "redirect:listboard";
	}
	
	@RequestMapping("/detailboard")
	public String detailboard(int boardnum, Model model ) {
		Board board = repository.selectOne(boardnum);
		int updateHitCount = repository.updateHitCount(boardnum);
		
		model.addAttribute("board", board);
		model.addAttribute("updateHitCount", updateHitCount);
		logger.info("조회수 증가 여부 :" + updateHitCount);
		return "board/detailBoard";
	}
	
	@RequestMapping("/deleteboard")
	public String deleteboard(int boardnum) {
		logger.info("삭제할 글번호 : " + boardnum);
		
		Board b = repository.selectOne(boardnum);
		String oldfile = b.getSavedfile();
		
		repository.delete(boardnum);
		
		//기존의 파일이 존재할 때 하드디스크에서 삭제
		if(oldfile != null) {
			String fullPath = uploadPath + "/" + oldfile;
			boolean result =FileService.deleteFile(fullPath);
			if(result) System.out.println("파일 삭제 완료");
			
		}
		
		
		return "redirect:listboard";
	}
	
	/**
	 * 수정할 수 있는 화면 요청
	 * @param boardnum
	 * @param model
	 * @return
	 */
	@RequestMapping("/updateboard")
	public String updateboard(int boardnum, Model model) {
		logger.info("수정할 글번호 : " + boardnum);
		Board board = repository.selectOne(boardnum);		
		model.addAttribute("board", board);
	
		return "board/updateBoard";
	}	
	
	@RequestMapping(value = "/updateboard", method=RequestMethod.POST)
	public String updateboard(Board board, MultipartFile upload) {
		
		Board b = repository.selectOne(board.getBoardnum());
		String oldfile = b.getSavedfile();
		
		
		//기존의 파일이 존재할 때 하드디스크에서 삭제
		
		if(oldfile != null) {
			String fullPath = uploadPath + "/" + oldfile;
			boolean result =FileService.deleteFile(fullPath);
			if(result) System.out.println("파일 삭제 완료");
			
		}
		if(!upload.isEmpty()) {
			String originalFilename = upload.getOriginalFilename();
			String savedFilename = FileService.saveFile(upload, uploadPath);
			
			board.setOriginalfile(originalFilename);
			board.setSavedfile(savedFilename);
		}
		
		int result = repository.update(board);
		
		return "redirect:listboard";
	}	
	@RequestMapping("/download")
	public void download(int boardnum, HttpServletResponse response) {
		Board board = repository.selectOne(boardnum);
		
		// 원래 파일명
		String originalFile = board.getOriginalfile();
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(originalFile,"UTF-8"));
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String fullPath = uploadPath + "/" + board.getSavedfile();
		
		FileInputStream filein = null;		// 서버가 HDD에서 메모리로 파일을 업로드할 때 사용
		ServletOutputStream fileout = null; // 클라이언트한테 통신으로 내보낼 때 사용하는 객체
		
		try {
			filein = new FileInputStream(fullPath);
			fileout = response.getOutputStream();
			
			FileCopyUtils.copy(filein, fileout);
			
			filein.close();
			fileout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 예약 화면
	 * @return
	 */
	@RequestMapping("/reservation")
	public String reservation() {
		
		return "board/reservation";
	}
	
	
}

/*
게시판 목록요청 : listboard

게시판 글쓰기  : (회원만 가능)
 writeboard GET : 화면요청
 writeboard POST : 글 등록 요청

게시글 자세히 보기 : detailboard 
게시글 삭제 : deleteboard (화면이 필요없음) (회원만 가능)
게시글 수정 : updateboard (회원만 가능)
*/