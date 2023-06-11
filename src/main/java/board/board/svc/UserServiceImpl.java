package board.board.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import board.board.dto.UserDto;
import board.mapper.BoardMapper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public boolean isUsernameExists(String username) {
        int count = boardMapper.selectCountByUsername(username);
		return count > 0;
	}

	@Override
	public void signUp(UserDto userDto) {
        boardMapper.insertUser(userDto);		
	}

	@Override
	public boolean login(String username, String password) {
		UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        int count = boardMapper.selectUserByUsernameAndPassword(userDto);
        return count > 0;	}

}
