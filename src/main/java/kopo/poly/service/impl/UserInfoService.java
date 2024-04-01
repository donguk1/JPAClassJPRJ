package kopo.poly.service.impl;


import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;



    @Override
    public UserInfoDTO getUserIdExists(String userId) throws Exception {

        log.info("service 아이디 중복 실행");

        UserInfoDTO rDTO;


        log.info("userId : " + userId);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder()
                    .existsYn("Y")
                    .build();

        } else {
            rDTO = UserInfoDTO.builder()
                    .existsYn("N")
                    .build();

        }

        log.info("service 아이디 중복확인 종료");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info("service 회원가입 실행");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password =  CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String addr1 = CmmUtil.nvl(pDTO.addr1());
        String addr2 = CmmUtil.nvl(pDTO.addr2());

        log.info("pDTO : " + pDTO);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity. isPresent()) {
            res = 2;

        } else {

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userName(userName)
                    .userId(userId)
                    .password(password)
                    .email(email)
                    .addr1(addr1)
                    .addr2(addr2)
                    .regId(userId)
                    .regDt(DateUtil.getDateTime("yyy-MM-dd hh:mm:ss"))
                    .chgId(userId)
                    .chgDt(DateUtil.getDateTime("yyy-MM-dd hh:mm:ss"))
                    .build();

            userInfoRepository.save(pEntity);

            rEntity = userInfoRepository.findByUserId(userId);

            if (rEntity. isPresent()) {
                res = 1;

            } else {
                res = 0;

            }

        }

        log.info("service 회원가입 종료");

        return res;
    }

    @Override
    public int getUserLogin(String userId, String password) throws Exception {

        log.info("service 로그인 실행");

        int res = 0;

        log.info("userId : " + userId);
        log.info("password : " + password);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndPassword(userId, password);

        if (rEntity.isPresent()) {
            res = 1;

        }

        log.info("service 로그인 종료");

        return res;
    }
}
