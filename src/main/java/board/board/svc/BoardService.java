package board.board.svc;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;


@Service
public interface BoardService {

	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto boardDto, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
	
	BoardDto selectBoardDetail(int boardIndex) throws Exception;
	
	void updateBoard(BoardDto boardDto) throws Exception;
	
	void deleteBoard(int boardIndex) throws Exception;
	
	BoardFileDto selectBoardFileInformation(int fileIndex, int boardIndex) throws Exception;
	
	void deleteBoardFile(int fileIndex, int boardIndex) throws Exception;
	
	int getBoardCount() throws Exception;
	
	
	List<BoardDto> selectBoardListWithPaging(int page, int pageSize) throws Exception;
	
	int getBoardCountByKeyword(String keyword);
	List<BoardDto> searchBoardByKeywordWithPaging(String keyword, int page, int pageSize) throws Exception;
}
