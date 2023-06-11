package board.board.svc;

import org.springframework.stereotype.Service;

import board.board.dto.UserDto;

@Service
public interface UserService {
	boolean isUsernameExists(String username);
	
	void signUp(UserDto userDto);
	
	boolean login(String username, String password);
}
