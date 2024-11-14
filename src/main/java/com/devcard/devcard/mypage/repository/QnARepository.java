package com.devcard.devcard.mypage.repository;

import com.devcard.devcard.mypage.entity.QnA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByOrderByQuestionTimestampDesc();

    QnA findQnAById(Long id);
}
