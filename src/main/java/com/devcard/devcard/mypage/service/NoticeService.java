package com.devcard.devcard.mypage.service;

import com.devcard.devcard.mypage.dto.NoticeRequest;
import com.devcard.devcard.mypage.dto.NoticeResponse;
import com.devcard.devcard.mypage.dto.NoticeUpdateRequest;
import com.devcard.devcard.mypage.entity.Notice;
import com.devcard.devcard.mypage.repository.NoticeRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<NoticeResponse> getNoticeList() {
        List<Notice> noticeList = noticeRepository.findAllByOrderByTimestampDesc();
        return noticeList.stream().map(notice -> new NoticeResponse(
            notice.getTitle(),
            notice.getContent(),
            notice.getTimestamp()
        )).toList();
    }

    public NoticeResponse getNotice(Long id) {
        Notice notice = noticeRepository.findNoticeById(id);
        return new NoticeResponse(notice.getTitle(), notice.getContent(), notice.getTimestamp());
    }

    public NoticeResponse addNotice(NoticeRequest noticeRequest) {
        Notice notice = noticeRepository.save(new Notice(noticeRequest.title(), noticeRequest.content(), LocalDateTime.now()));
        return new NoticeResponse(notice.getTitle(), notice.getContent(), notice.getTimestamp());
    }

    public NoticeResponse updateNotice(NoticeUpdateRequest noticeUpdateRequest) {
        Notice notice = noticeRepository.save(new Notice(noticeUpdateRequest.id(), noticeUpdateRequest.title(), noticeUpdateRequest.content(), LocalDateTime.now()));
        return new NoticeResponse(notice.getTitle(), notice.getContent(), notice.getTimestamp());
    }

    public NoticeResponse deleteNotice(Long id) {
        Notice deleteNotice = noticeRepository.findNoticeById(id);
        noticeRepository.delete(deleteNotice);
        return new NoticeResponse(deleteNotice.getTitle(), deleteNotice.getContent(), deleteNotice.getTimestamp());
    }
}
