package kopo.poly.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeFetchRepository;
import kopo.poly.repository.NoticeJoinRepository;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.NoticeSQLRepository;
import kopo.poly.repository.entity.*;
import kopo.poly.service.INoticeJoinService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeJoinService implements INoticeJoinService {

    private final NoticeRepository noticeRepository;

    private final NoticeJoinRepository noticeJoinRepository;

    private final NoticeSQLRepository noticeSQLRepository;

    private final NoticeFetchRepository noticeFetchRepository;

    private final JPAQueryFactory queryFactory;


    @Override
    public List<NoticeDTO> getNoticeListUsingJoinColumn() {

        log.info("service noticeJoinColumn 실행");

        List<NoticeJoinEntity> rList = noticeJoinRepository.findAllByOrderByNoticeSeqDesc();

        List<NoticeDTO> list = new LinkedList<>();

        rList.forEach(rEntity -> {

            long noticeSeq = rEntity.getNoticeSeq();
            String noticeYn = CmmUtil.nvl(rEntity.getNoticeYn());
            String title = CmmUtil.nvl(rEntity.getTitle());
            long readCnt = rEntity.getReadCnt();
            String userName = CmmUtil.nvl(rEntity.getUserInfoEntity().getUserName());
            String regDt = CmmUtil.nvl(rEntity.getRegDt());

            log.info("noticeSeq : " + noticeSeq);
            log.info("noticeYn : " + noticeYn);
            log.info("title : " + title);
            log.info("readCnt : " + readCnt);
            log.info("userName : " + userName);
            log.info("regDt : " + regDt);


            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(noticeSeq)
                    .noticeYn(noticeYn)
                    .title(title)
                    .readCnt(readCnt)
                    .userName(userName)
                    .regDt(regDt)
                    .build();

            list.add(rDTO);
        });

        log.info("service noticeJoinColumn 종료");

        return list;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingNativeQuery() {

        log.info("service 네이티브 쿼리 실행");

        List<NoticeSQLEntity> rList = noticeSQLRepository.getNoticeListUsingSQL();

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<NoticeDTO>>() {
                });


        return nList;
    }

    @Override
    public List<NoticeDTO> getNoticeListUsingJPQL() {

        log.info("service NoticeFetchEntity");

        List<NoticeFetchEntity> rList = noticeFetchRepository.getListFetchJoin();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq())
                    .title(e.getTitle())
                    .noticeYn(e.getNoticeYn())
                    .readCnt(e.getReadCnt())
                    .userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName())
                    .build();

            nList.add(rDTO);
        });

        log.info("service NoticeFetchEntity");

        return nList;
    }

    @Transactional
    @Override
    public List<NoticeDTO> getNoticeListForQueryDSL() {

        log.info("service QueryDSL");

        QNoticeFetchEntity ne = QNoticeFetchEntity.noticeFetchEntity;
        QUserInfoEntity ue = QUserInfoEntity.userInfoEntity;

        List<NoticeFetchEntity> rList = queryFactory
                .selectFrom(ne)
                .join(ne.userInfo, ue)
                .orderBy(ne.noticeYn.desc(), ne.noticeSeq.desc())
                .fetch();

        List<NoticeDTO> nList = new ArrayList<>();

        rList.forEach(e -> {
            NoticeDTO rDTO = NoticeDTO.builder()
                    .noticeSeq(e.getNoticeSeq())
                    .title(e.getTitle())
                    .noticeYn(e.getNoticeYn())
                    .chgDt(e.getChgDt())
                    .regDt(e.getRegDt())
                    .readCnt(e.getReadCnt())
                    .userId(e.getUserId())
                    .userName(e.getUserInfo().getUserName())
                    .build();

            nList.add(rDTO);
        });

        log.info("service QueryDSL");

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfoForQueryDSL(NoticeDTO pDTO, boolean type) throws Exception {

        log.info("service QDSL INFO");

        if (type) {
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            log.info("res : " + res);

        }

        QNoticeEntity ne = QNoticeEntity.noticeEntity;

        NoticeEntity rEntity = queryFactory
                .selectFrom(ne)
                .where(ne.noticeSeq.eq(pDTO.noticeSeq()))
                .fetchOne();

        NoticeDTO rDTO = NoticeDTO.builder()
                .noticeSeq(rEntity.getNoticeSeq())
                .title(rEntity.getTitle())
                .regDt(rEntity.getRegDt())
                .chgDt(rEntity.getChgDt())
                .userId(rEntity.getUserId())
                .readCnt(rEntity.getReadCnt())
                .contents(rEntity.getContents())
                .build();

        log.info("service QDSL INFO");

        return rDTO;
    }
}
