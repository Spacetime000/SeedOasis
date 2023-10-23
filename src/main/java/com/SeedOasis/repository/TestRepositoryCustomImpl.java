package com.SeedOasis.repository;

import com.SeedOasis.constant.Direction;
import com.SeedOasis.constant.TestCategory;
import com.SeedOasis.dto.AdminTestListDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.dto.TestSearchDto;
import com.SeedOasis.entity.QMember;
import com.SeedOasis.entity.QTest;
import com.SeedOasis.entity.QTestLike;
import com.SeedOasis.entity.Test;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

public class TestRepositoryCustomImpl implements TestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TestRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchCategory(TestCategory category) {
        if (category == null) {
            return null;
        } else {
            return QTest.test.testCategory.eq(category);
        }
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QTest.test.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QTest.test.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    private OrderSpecifier<?> sort(SortDto sortDto) {

        if (sortDto == null) {
            return QTest.test.testId.asc();
        }

        ComparableExpressionBase<?> orderByExpression = null;
        String target = sortDto.getTarget();

        switch (target) {
            case "id":
                orderByExpression = QTest.test.testId;
                break;
            case "title":
                orderByExpression = QTest.test.title;
                break;
            case "views":
                orderByExpression = QTest.test.views;
                break;
            case "like":
                orderByExpression = QTest.test.testLikes.size();
                break;
            case "createBy":
                orderByExpression = QTest.test.createBy;
                break;
        }

        if (orderByExpression != null) {
            return (sortDto.getDirection() == Direction.ASC) ? orderByExpression.asc() : orderByExpression.desc();
        }

        return QTest.test.testId.asc();
    }

/*
    private BooleanExpression memberId(String memberId) {
        return memberId != null ? QTest.test.createBy.like(memberId) : null;
    }
*/

    @Override
    public Page<Test> getTestListPage(Pageable pageable, TestSearchDto testSearchDto) {

//        BooleanExpression categoryPredicate = searchCategory(testSearchDto.getCategory());

        List<Test> result = jpaQueryFactory
                .selectFrom(QTest.test)
                .where(searchCategory(testSearchDto.getCategory()), searchByLike(testSearchDto.getSearchBy(), testSearchDto.getSearchQuery()), QTest.test.enabled.eq(Boolean.TRUE))
                .orderBy(QTest.test.testId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QTest.test)
                .where(searchCategory(testSearchDto.getCategory()), searchByLike(testSearchDto.getSearchBy(), testSearchDto.getSearchQuery()), QTest.test.enabled.eq(Boolean.TRUE))
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<AdminTestListDto> testListPage(Pageable pageable, TestSearchDto testSearchDto, SortDto sortDto) {
        List<AdminTestListDto> result = jpaQueryFactory
                .select(
                        Projections.constructor(AdminTestListDto.class,
                                QTest.test.testId,
                                QTest.test.title,
                                QTest.test.testCategory,
                                QTest.test.views,
                                QTest.test.testLikes.size(),
                                QTest.test.createTime,
                                QTest.test.createBy,
                                QTest.test.enabled,
                                QMember.member.name
                                )
                        )
                .from(QTest.test)
                .join(QMember.member).on(QMember.member.memberId.eq(QTest.test.createBy))
                .where(searchByLike(testSearchDto.getSearchBy(), testSearchDto.getSearchQuery()), searchCategory(testSearchDto.getCategory()))
                .orderBy(sort(sortDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QTest.test)
                .join(QMember.member).on(QMember.member.memberId.eq(QTest.test.createBy))
                .where(searchByLike(testSearchDto.getSearchBy(), testSearchDto.getSearchQuery()), searchCategory(testSearchDto.getCategory()))
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }
}
