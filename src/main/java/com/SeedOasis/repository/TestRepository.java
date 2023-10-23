package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminGraph;
import com.SeedOasis.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long>, TestRepositoryCustom {

    //    List<Test> findByCreateBy(String memberId);
    Page<Test> findByCreateBy(String memberId, Pageable pageable);

    @Query("SELECT t FROM Test t JOIN TestLike l ON t.testId = l.test.testId WHERE l.member.memberId = :memberId")
    Page<Test> testLikePage(@Param("memberId") String memberId, Pageable pageable);

    @Query("SELECT t FROM Test t ORDER BY t.testId DESC")
    List<Test> findTop5Tests(Pageable pageable);

    @Query("SELECT DATE(t.createTime) AS time, COUNT(t) AS count " +
            "FROM Test t " +
            "WHERE t.createTime BETWEEN :sDate AND :eDate " +
            "GROUP BY time " +
            "ORDER BY time")
    List<AdminGraph> getTestLastWeekCount(@Param("sDate") LocalDateTime sDate, @Param("eDate") LocalDateTime eDate);

    Long countBy();

    @Query("SELECT COUNT(t) " +
            "FROM Test t " +
            "WHERE DATE(t.createTime) = CURDATE()")
    Long getTodayCount();
}
