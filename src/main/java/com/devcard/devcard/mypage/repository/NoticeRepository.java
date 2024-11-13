package com.devcard.devcard.mypage.repository;

import com.devcard.devcard.mypage.entity.Notice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByTimestampDesc();

    Notice findNoticeById(Long id);
}
