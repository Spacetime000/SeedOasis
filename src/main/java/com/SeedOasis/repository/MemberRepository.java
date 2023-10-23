package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminGraph;
import com.SeedOasis.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String>, MemberCustom {
    boolean existsByMemberId(String memberId);
    boolean existsByEmail(String email);
    boolean existsByName(String name);

    @Query("SELECT m.name FROM Member m WHERE m.id = :testId")
    String getName(@Param("testId") String id);

/*    @Query(value = "SELECT DATE(create_time) AS time, COUNT(*) AS count " +
            "FROM member " +
            "WHERE create_time BETWEEN CURDATE() - INTERVAL 6 DAY AND CURDATE() " +
            "GROUP BY time " +
            "ORDER BY time",
            nativeQuery = true)*/

    @Query("SELECT DATE(m.createTime) AS time, COUNT(m) AS count " +
            "FROM Member m " +
            "WHERE m.createTime BETWEEN :sDate AND :eDate " +
            "GROUP BY time " +
            "ORDER BY time")
    List<AdminGraph> getMemberLastWeekCount(@Param("sDate") LocalDateTime sDate, @Param("eDate") LocalDateTime eDate);

    @Query("SELECT COUNT(m) " +
            "FROM Member m " +
            "WHERE DATE(m.createTime) = CURDATE()")
    Long getTodayCount();
}
