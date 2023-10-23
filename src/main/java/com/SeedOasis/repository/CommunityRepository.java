package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminGraph;
import com.SeedOasis.entity.Community;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityCustom {

/*    @Query("SELECT c FROM Community c")
    Page<Community> communityList(Pageable pageable);*/

    @Query("SELECT c FROM Community c ORDER BY c.id DESC")
    List<Community> findTop5Community(Pageable pageable);

    @Query("SELECT DATE(c.createTime) AS time, COUNT(c) AS count " +
            "FROM Community c " +
            "WHERE c.createTime BETWEEN :sDate AND :eDate " +
            "GROUP BY time " +
            "ORDER BY time")
    List<AdminGraph> getCommunityLastWeekCount(@Param("sDate") LocalDateTime sDate, @Param("eDate") LocalDateTime eDate);

    @Query("SELECT COUNT(c) " +
            "FROM Community c " +
            "WHERE DATE(c.createTime) = CURDATE()")
    Long getTodayCount();
}
