package board.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data //@getter, @setter, @toString @RequiredArgsConstructor 등을 합쳐놓은듯한 것. + @EqualsAndHashCode (?)
public class BoardDto {

	private int boardIndex;
	
	private String title;
	
	private String contents;
	
	private int hitCount;
	
	private LocalDateTime createdDatetime;

	private String creatorId;

	private LocalDateTime updatedDatetime;
	
	private String updaterId;
	
	private List<BoardFileDto> fileList;
	
}
