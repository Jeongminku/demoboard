package board.board.svc;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.common.FileUtils;
import board.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	

	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}

	@Override
	public void insertBoard(BoardDto boardDto, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		boardMapper.insertBoard(boardDto);
		if(ObjectUtils.isEmpty(multipartHttpServletRequest) == false) {
			List<BoardFileDto> list = fileUtils.parseFileInfo(boardDto.getBoardIndex(), multipartHttpServletRequest);
			if(CollectionUtils.isEmpty(list) == false) {
				boardMapper.insertBoardFileList(list);
			}
		}
	}

	@Override
	public BoardDto selectBoardDetail(int boardIndex) throws Exception {
		BoardDto boardDto = boardMapper.selectBoardDetail(boardIndex); 
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIndex);
		//BoardDto boardDto = boardMapper.selectBoardDetail(boardIndex);
		boardDto.setFileList(fileList);
		
		boardMapper.updateHitCount(boardIndex);
		
		return boardDto;
	}


	@Override
	public void updateBoard(BoardDto boardDto) throws Exception {
		boardMapper.updateBoard(boardDto);
	}

	@Override
	public void deleteBoard(int boardIndex) throws Exception {
		boardMapper.deleteBoard(boardIndex);
	}
	
	@Override
	public BoardFileDto selectBoardFileInformation(int fileIndex, int boardIndex) throws Exception {
		return boardMapper.selectBoardFileInformation(fileIndex, boardIndex);
	}

	
	@Override
	public void deleteBoardFile(int fileIndex, int boardIndex) throws Exception {
		boardMapper.deleteBoardFile(fileIndex, boardIndex);
		
	}

	@Override
	public int getBoardCount() throws Exception {
		return boardMapper.getBoardCount();
	}

	@Override
	public List<BoardDto> selectBoardListWithPaging(int page, int pageSize) throws Exception {

		int pageOffset = (page - 1) * pageSize;
		
		if (pageOffset < 0) {
			pageOffset = 0;
		}
		
		return boardMapper.selectBoardListWithPaging(pageOffset, pageSize);
	}

	@Override
	public int getBoardCountByKeyword(String keyword) {
		return boardMapper.getBoardCountByKeyword(keyword);
	}

	@Override
	public List<BoardDto> searchBoardByKeywordWithPaging(String keyword, int page, int pageSize) throws Exception {
		
		int pageOffset = (page - 1) * pageSize;
		
		if (pageOffset < 0) {
			pageOffset = 0;
		}
		return boardMapper.searchBoardByKeywordWithPaging(keyword, pageOffset, pageSize);
	}

	
	
}
