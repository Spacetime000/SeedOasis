package com.SeedOasis.repository;

import com.SeedOasis.entity.Test;
import com.SeedOasis.entity.TestLike;
import com.SeedOasis.entity.TestLikeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestLikeRepository extends JpaRepository<TestLike, TestLikeId> {

    /*@Query("SELECT t FROM Test t JOIN TestLike l ON t.testId = l.test WHERE l.member = :memberId")
    Page<Test> testLikePage(@Param("memberId") String memberId, Pageable pageable);*/
}
