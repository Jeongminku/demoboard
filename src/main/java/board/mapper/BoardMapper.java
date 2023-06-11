package board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.dto.UserDto;

@Mapper
public interface BoardMapper {
	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto boardDto) throws Exception;
	
	void updateHitCount(int boardIndex) throws Exception;
	
	BoardDto selectBoardDetail(int boardIndex) throws Exception;
	
	void updateBoard(BoardDto boardDto) throws Exception;
	
	void deleteBoard(int boardIndex) throws Exception;
	
	void insertBoardFileList(List<BoardFileDto> list) throws Exception;
	
	List<BoardFileDto> selectBoardFileList(int boardIndex) throws Exception;
	
	BoardFileDto selectBoardFileInformation(@Param("fileIndex") int fileIndex, @Param("boardIndex") int boardIndex);
	
	void deleteBoardFile(@Param("fileIndex") int fileIndex, @Param("boardIndex") int boardIndex);
	
	
	int getBoardCount() throws Exception;
	
	List<BoardDto> selectBoardListWithPaging(@Param("pageOffset")int pageOffset, @Param("pageSize")int pageSize);
	
	
	
	
	void insertUser(UserDto userDto);
	int selectCountByUsername(String username);
    int selectUserByUsernameAndPassword(UserDto userDto);
    
    
    int getBoardCountByKeyword(String keyword);
	List<BoardDto> searchBoardByKeywordWithPaging(@Param("keyword") String keyword, @Param("pageOffset")int pageOffset, @Param("pageSize") int pageSize) throws Exception;
}
