package com.devcard.devcard.auth.repository;

import com.devcard.devcard.auth.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByGithubId(String githubId);

    List<Member> findByIdIn(List<Long> ids);
}
