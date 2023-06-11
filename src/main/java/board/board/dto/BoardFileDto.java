package board.board.dto;

import lombok.Data;

@Data
public class BoardFileDto {
	private int fileIndex;
	
	private int boardIndex;
	
	private String originalFileName;
	
	private String storedFilePath;
	
	private long fileSize;
}
