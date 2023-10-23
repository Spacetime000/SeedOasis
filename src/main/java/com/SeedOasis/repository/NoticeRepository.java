package com.SeedOasis.repository;

import com.SeedOasis.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    @Query("SELECT n.content FROM Notice n WHERE n.id = :noticeId")
    String getContent(@Param("noticeId") Long noticeId);

    @Query("SELECT n FROM Notice n ORDER BY n.noticeId DESC")
    List<Notice> findTop5Notices(Pageable pageable);
}
