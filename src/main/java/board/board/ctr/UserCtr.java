package board.board.ctr;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import board.board.dto.UserDto;
import board.board.svc.UserService;

@Controller
@RequestMapping("/board")
public class UserCtr {

	@Autowired
	private UserService userService;
	
	@GetMapping("/signup")
	public ModelAndView signUpForm() {
		ModelAndView mav = new ModelAndView("/board/signup");
		mav.addObject("userDto", new UserDto());
		return mav;
	}
	
	@PostMapping("/signup")
	public ModelAndView signUp(@ModelAttribute("userDto") UserDto userDto) {
		ModelAndView mav = new ModelAndView();
		
		if (userService.isUsernameExists(userDto.getUsername())) {
			mav.setViewName("redirect:/board/signup");
			mav.addObject("error","Username already exists");
		} else {
			userService.signUp(userDto);
			mav.setViewName("redirect:/board/login");
		}
		
		return mav;
	}
	
	@GetMapping("/login")
	public ModelAndView loginForm() {
		ModelAndView mav = new ModelAndView("/board/login");
		mav.addObject("userDto", new UserDto());
		return mav;
	}
	
	@PostMapping("/login")
	public ModelAndView login (@ModelAttribute("userDto") UserDto userDto, HttpSession session, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		if (userService.login(userDto.getUsername(), userDto.getPassword())) {
			// 로그인 성공 시 세션에 사용자 정보 저장
			session.setAttribute("userDto", userDto);

			// 세션 ID를 쿠키에 저장
	        Cookie cookie = new Cookie("sessionId", session.getId());
	        cookie.setMaxAge(3600); // 쿠키 유효기간 설정 (예: 1시간)
	        cookie.setPath("/"); // 쿠키의 유효 경로 설정
	        response.addCookie(cookie);
	        
			mav.setViewName("forward:/board/openBoardList.do");
		} else {
			mav.setViewName("redirect:/board/login");
			mav.addObject("error", "Invalid username or password");
		}
		return mav;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		//세션에서 사용자 정보 제거
		session.invalidate();
		
		//쿠기 삭제
		Cookie sessionIdCookie = new Cookie("sessionId", null);
		sessionIdCookie.setMaxAge(0);
		sessionIdCookie.setPath("/");
		response.addCookie(sessionIdCookie);
		
		return "redirect:/board/login";
	}
}
