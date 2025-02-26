package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
