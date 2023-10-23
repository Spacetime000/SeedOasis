package com.SeedOasis.repository;

import com.SeedOasis.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long>, CommunityCommentCustom {
}
