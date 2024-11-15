package com.devcard.devcard.chat.repository;

import com.devcard.devcard.chat.model.ChatRoom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.id = :userId")
    List<ChatRoom> findByParticipantId(@Param("userId") String userId);

    @Query("SELECT cr FROM ChatRoom cr " +
        "WHERE SIZE(cr.participants) = :size " +
        "AND cr.id IN (" +
        "    SELECT cr2.id FROM ChatRoom cr2 " +
        "    JOIN cr2.participants p " +
        "    WHERE p.id IN :participantsId " +
        "    GROUP BY cr2.id " +
        "    HAVING COUNT(p.id) = :size" +
        ")")
    Optional<ChatRoom> findByExactParticipants(@Param("participantsId") List<Long> participantsId,
        @Param("size") int size);
}
