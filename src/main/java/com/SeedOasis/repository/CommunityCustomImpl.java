package com.SeedOasis.repository;

import com.SeedOasis.constant.CommunityCategory;
import com.SeedOasis.constant.Direction;
import com.SeedOasis.dto.AdminCommunityListDto;
import com.SeedOasis.dto.CommunitySearchDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.entity.Community;
import com.SeedOasis.entity.QCommunity;
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

public class CommunityCustomImpl implements CommunityCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommunityCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchCategory(CommunityCategory communityCategory) {
        if (communityCategory == null)
            return null;
        else
            return QCommunity.community.communityCategory.eq(communityCategory);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy))
            return QCommunity.community.title.like("%" + searchQuery + "%");
        else if (StringUtils.equals("createBy", searchBy))
            return QCommunity.community.createBy.like("%" + searchQuery + "%");

        return null;
    }

    //id, 작성자 id, 제목, 닉네임
    private BooleanExpression search(String query) {
        return QCommunity.community.id.like(like(query))
                .or(QCommunity.community.title.like(like(query)))
                .or(QCommunity.community.createBy.like(like(query)))
                .or(QMember.member.name.like(like(query)));
    }

    private OrderSpecifier<?> sort(SortDto sortDto) {

        if (sortDto == null) {
            return QCommunity.community.id.asc();
        }

        ComparableExpressionBase<?> orderByExpression = null;
        String target = sortDto.getTarget();

        switch (target) {
            case "id":
                orderByExpression = QCommunity.community.id;
                break;
            case "title":
                orderByExpression = QCommunity.community.title;
                break;
            case "createBy" :
                orderByExpression = QCommunity.community.createBy;
                break;
            case "name" :
                orderByExpression = QMember.member.name;
                break;
            case "createTime":
                orderByExpression = QCommunity.community.createTime;
                break;
            case "view" :
                orderByExpression = QCommunity.community.views;
                break;
        }

        if (orderByExpression != null) {
            return (sortDto.getDirection() == Direction.ASC) ? orderByExpression.asc() : orderByExpression.desc();
        }

        return QCommunity.community.id.asc();
    }

    /**
     * 검색
     * @param query
     * @return "%" + query + "%"
     */
    private static String like(String query) {
        return "%" + query + "%";
    }

    @Override
    public Page<Community> communityListPage(Pageable pageable, CommunitySearchDto communitySearchDto) {

        List<Community> result = jpaQueryFactory
                .selectFrom(QCommunity.community)
                .where(searchCategory(communitySearchDto.getCommunityCategory()), searchByLike(communitySearchDto.getSearchBy(), communitySearchDto.getSearchQuery()))
                .orderBy(QCommunity.community.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QCommunity.community)
                .where(searchCategory(communitySearchDto.getCommunityCategory()), searchByLike(communitySearchDto.getSearchBy(), communitySearchDto.getSearchQuery()))
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<AdminCommunityListDto> communityListPage(Pageable pageable, CommunitySearchDto communitySearchDto, SortDto sortDto) {
        List<AdminCommunityListDto> result = jpaQueryFactory
                .select(Projections.constructor(AdminCommunityListDto.class,
                        QCommunity.community.id,
                        QCommunity.community.communityCategory,
                        QCommunity.community.title,
                        QCommunity.community.createBy,
                        QCommunity.community.createTime,
                        QCommunity.community.views,
                        QMember.member.name))
                .from(QCommunity.community)
                .join(QMember.member).on(QMember.member.memberId.eq(QCommunity.community.createBy))
                .where(search(communitySearchDto.getSearchQuery()), searchCategory(communitySearchDto.getCommunityCategory()))
                .orderBy(sort(sortDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QCommunity.community)
                .join(QMember.member).on(QMember.member.memberId.eq(QCommunity.community.createBy))
                .where(search(communitySearchDto.getSearchQuery()), searchCategory(communitySearchDto.getCommunityCategory()))
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }
}
