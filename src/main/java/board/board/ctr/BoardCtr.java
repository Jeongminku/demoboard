package board.board.ctr;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.svc.BoardService;
import board.board.svc.UserService;

@Controller
public class BoardCtr {
	
	//로그 사용하기.
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BoardService boardService;
	
	/*
	@RequestMapping(value = {"/board/openBoardList.do", "/"})
	public ModelAndView openBoardList() throws Exception {
		
		log.debug("openBoardList 로그.디버그 테스트");
		
		ModelAndView mav = new ModelAndView("/board/boardList");
		
		List<BoardDto> list = boardService.selectBoardList();	
		mav.addObject("list", list);
		
		return mav;
	}
	*/
	
	@RequestMapping(value = {"/board/openBoardList.do", "/"})
	public ModelAndView openBoardList(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword, HttpServletRequest request) throws Exception {
		
		//session.getAttribute("isLoggedIn");
		HttpSession session = request.getSession();
		session.setAttribute("currentPage", page);
		session.setAttribute("keyword", keyword);
		
		int pageSize = 4; // 페이지당 글 개수

		int totalBoardCount;
		List<BoardDto> list;
		
		if (keyword != null && !keyword.isEmpty()) {
			//keyword로 제목검색
			totalBoardCount = boardService.getBoardCountByKeyword(keyword);
			list = boardService.searchBoardByKeywordWithPaging(keyword, page, pageSize);
			System.out.println("리스트값:==========================="+list);
		} else {
			//전체 글 개수
			totalBoardCount = boardService.getBoardCount();
			list = boardService.selectBoardListWithPaging(page, pageSize);
		}
		
		
	    //int totalBoardCount = boardService.getBoardCount(); // 전체 글 개수
	    int totalPages = (int) Math.ceil((double) totalBoardCount / pageSize); // 총 페이지 개수
		
	    //현재 페이지 번호 보정
	    if (page < 1) {
	        page = 1;
	    } else if (page > totalPages) {
	        page = totalPages;
	    }
	    
	    //List<BoardDto> list = boardService.selectBoardListWithPaging(page, pageSize);
	    
		ModelAndView mav = new ModelAndView("/board/boardList");
		
		mav.addObject("list", list);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		
		List<Integer> pageNumbers = new ArrayList<>();
	    for (int i = 1; i <= totalPages; i++) {
	        pageNumbers.add(i);
	    }
	    mav.addObject("pageNumbers", pageNumbers);
		mav.addObject("keyword", keyword);
		return mav;
	}
	
	
	
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception {
		return "board/boardWrite";
	}
	
	@RequestMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		boardService.insertBoard(board, multipartHttpServletRequest);

		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam int boardIndex, HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		Integer currentPage = (Integer) session.getAttribute("currentPage");
		String keyword = (String) session.getAttribute("keyword");
		
		ModelAndView mav = new ModelAndView("/board/boardDetail");
		BoardDto boardDto = boardService.selectBoardDetail(boardIndex);
		mav.addObject("board", boardDto);
		mav.addObject("currentPage",currentPage);
		mav.addObject("keyword", keyword);
		System.err.println(keyword);
		
		return mav;
	}
	
	/*
	 @RequestMapping("/board/updateBoard.do") 
	 public String updateBoard(BoardDto boardDto) throws Exception { 
	
		 boardService.updateBoard(boardDto);
	 
		 return "redirect:/board/openBoardList.do";
	  
	  }
	 */ 
	 
	 @RequestMapping("/board/updateBoard.do") 
	 	public ResponseEntity<String> updateBoard(BoardDto boardDto) throws Exception { 
		
		boardService.updateBoard(boardDto);
		 
		return ResponseEntity.ok("게시글이 수정되었습니다.");
		 
	 }
	 

	
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIndex) throws Exception {
		boardService.deleteBoard(boardIndex);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/downloadBoardFile.do")
	public void downloadBoardFile(@RequestParam int fileIndex, @RequestParam int boardIndex, HttpServletResponse response) throws Exception {
		BoardFileDto boardFile = boardService.selectBoardFileInformation(fileIndex, boardIndex);
		if (ObjectUtils.isEmpty(boardFile)==false) {
			String fileName = boardFile.getOriginalFileName();
			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
			
			response.setContentType("application/octet-stream"); //response의 헤더에 contentType, 크기 및 형태를 설정. 파일은 반드시 UTF-8로 인코딩.
			response.setContentLength(files.length);
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8")+"\";");
			
			response.getOutputStream().write(files);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	@RequestMapping("board/deleteBoardFile.do")
	public String deleteBoardFile(@RequestParam int fileIndex, @RequestParam int boardIndex) throws Exception {
		boardService.deleteBoardFile(fileIndex, boardIndex);
		System.out.println(boardIndex);
		return "redirect:/board/openBoardDetail.do?boardIndex="+boardIndex;
	}
}
