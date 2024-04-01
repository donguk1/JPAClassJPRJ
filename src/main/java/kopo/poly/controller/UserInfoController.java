package kopo.poly.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.impl.UserInfoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "user")
@Controller
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info("controller 회원가입 화면으로 이동");

        return "/user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info("controller 아이디 중복체크 실행");

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId : " + userId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(userId))
                .orElseGet(() ->
                        UserInfoDTO.builder().build());

        log.info("controller 아이디 중복체크 완료");

        return rDTO;
    }


    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info("controller 회원가입 실행");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String password = CmmUtil.nvl(request.getParameter("password"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
        String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("password : " + password);
        log.info("email : " + email);
        log.info("addr1 : " + addr1);
        log.info("addr2 : " + addr2);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userName(userName)
                .userId(userId)
                .password(password)
                .email(email)
                .addr1(addr1)
                .addr2(addr2)
                .build();

        int res = userInfoService.insertUserInfo(pDTO);

        log.info("회원가입 결과(res) : " + res);

        if (res == 1) {
            msg = "회원가입 되었습니다.";

        } else if (res == 2) {
            msg = "이미 가입된 회원입니다.";

        } else {
            msg = "오류로 인해 회원가입이 실패하였습니다.";

        }

        MsgDTO dto = MsgDTO.builder()
                .msg(msg)
                .result(res)
                .build();

        log.info("controller 회원가입 완료");

        return dto;
    }

    @GetMapping(value = "login")
    public String login() {

        log.info("controller 로그인 화면으로 이동");

        return "/user/login";
    }

    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("controller 로그인 실행");

        String msg;
        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("userId : " + userId);
        log.info("password : " + password);

        int res = userInfoService.getUserLogin(userId, password);

        if (res == 1) {
            msg = "로그인이 성공했습니다.";
            session.setAttribute("SS_USER_ID", userId);

        } else {
            msg = "아이디와 비밀번호가 올바르지 않습니다.";

        }

        MsgDTO dto = MsgDTO.builder()
                .result(res)
                .msg(msg)
                .build();

        log.info("controller 로그인 완료");

        return dto;
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {

        log.info("controller 로그인 성공 화면 이동" );

        return "/user/loginSuccess";
    }

    @ResponseBody
    @PostMapping(value = "logout")
    public MsgDTO logout(HttpSession session) {

        log.info("controller 로그아웃 실행");

        session.setAttribute("SS_USER_ID", "");
        session.removeAttribute("SS_USER_ID");

        MsgDTO dto = MsgDTO.builder()
                .msg("로그아웃 하였습니다.")
                .build();

        log.info("controller 로그아웃 완료");

        return dto;

    }



}
