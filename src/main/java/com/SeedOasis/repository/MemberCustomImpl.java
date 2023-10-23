package com.SeedOasis.repository;

import com.SeedOasis.constant.Direction;
import com.SeedOasis.dto.AdminMemberListDto;
import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.entity.QMember;
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

public class MemberCustomImpl implements MemberCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private OrderSpecifier<?> sort(SortDto sortDto) {
        if (sortDto == null) {
            return QMember.member.memberId.asc();
        }

        ComparableExpressionBase<?> orderByExpression = null;
        String target = sortDto.getTarget();

        switch (target) {
            case "id":
                orderByExpression = QMember.member.memberId;
                break;
            case "createTime":
                orderByExpression = QMember.member.createTime;
                break;
            case "email":
                orderByExpression = QMember.member.email;
                break;
            case "name":
                orderByExpression = QMember.member.name;
                break;
            default:
                orderByExpression = QMember.member.memberId;
                break;
        }

        if (orderByExpression != null) {
            return (sortDto.getDirection() == Direction.ASC) ? orderByExpression.asc() : orderByExpression.desc();
        }

        return QMember.member.memberId.asc();
    }

    @Override
    public Page<AdminMemberListDto> memberPage(Pageable pageable, SortDto sortDto) {
        List<AdminMemberListDto> result = jpaQueryFactory
                .select(Projections.constructor(AdminMemberListDto.class,
                        QMember.member.memberId,
                        QMember.member.email,
                        QMember.member.avatar,
                        QMember.member.name,
                        QMember.member.role,
                        QMember.member.createTime))
                .from(QMember.member)
                .orderBy(sort(sortDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QMember.member)
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }
}
