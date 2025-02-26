package erick.projects.socialnetwork.repository;

import erick.projects.socialnetwork.model.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    boolean existsByMemberIdAndGroupId(Long memberId, Long groupId);
}
