package kopo.poly.controller;


import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/notice")
public class NoticeJoinController {


    private final INoticeJoinService noticeJoinService;


    @GetMapping(value = "noticeListUsingJoinColumn")
    public String noticeListUsingJoinColumn(HttpSession session, ModelMap modelMap) throws Exception {

        log.info("controller 페이지 이동");

        session.setAttribute("SS_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListUsingJoinColumn()).orElseGet(ArrayList::new);

        modelMap.addAttribute("rList", rList);

        return "notice/noticeListJoin";


    }


    @GetMapping(value = "noticeListUsingNativeQuery")
    public String noticeListUsingNativeQuery(HttpSession session, ModelMap modelMap) throws Exception {

        log.info("controller 페이지 이동");

        session.setAttribute("SS_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListUsingNativeQuery()).orElseGet(ArrayList::new);

        modelMap.addAttribute("rList", rList);

        return "notice/noticeListJoin";

    }

    @GetMapping(value = "noticeListUsingJPQL")
    public String noticeListUsingJPQL(HttpSession session, ModelMap modelMap) throws Exception {

        log.info("controller 페이지 이동");

        session.setAttribute("SS_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListUsingJPQL()).orElseGet(ArrayList::new);

        modelMap.addAttribute("rList", rList);

        return "notice/noticeListJoin";

    }

    @GetMapping(value = "noticeListUsingQueryDSL")
    public String noticeListUsingQueryDSL(HttpSession session, ModelMap modelMap) throws Exception {

        log.info("controller 페이지 이동");

        session.setAttribute("SS_USER_ID", "USER01");

        List<NoticeDTO> rList = Optional.ofNullable(noticeJoinService.getNoticeListForQueryDSL()).orElseGet(ArrayList::new);

        modelMap.addAttribute("rList", rList);

        return "notice/noticeListJoin";

    }



}
