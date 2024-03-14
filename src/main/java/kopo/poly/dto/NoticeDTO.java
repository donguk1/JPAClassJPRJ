package kopo.poly.dto;

import lombok.Builder;

@Builder
public record NoticeDTO(

        Long noticeSeq,
        String title,
        String noticeYn,
        String contents,
        String userId,
        String Cnt,
        String regId,
        String regDt,
        String chgId,
        String chgDt,
        String userName
) {
}
