package com.devcard.devcard.card.repository;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.entity.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByMember(Member member);

    Optional<Group> findByIdAndMember(Long id, Member member);

    @Query(
        "SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
            "FROM Group g " +
            "JOIN g.cards c " +
            "WHERE g.member = :member AND c.member.id = :cardOwnerId"
    )
    boolean existsByMemberAndCards_Id(@Param("member") Member member, @Param("cardOwnerId") Long cardOwnerId);
}
