package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminCommentListDto;
import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.entity.QCommunityComment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CommunityCommentCustomImpl implements CommunityCommentCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommunityCommentCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<AdminCommentListDto> commentListPage(Pageable pageable) {
        List<AdminCommentListDto> result = jpaQueryFactory
                .select(Projections.constructor(AdminCommentListDto.class,
                        QCommunityComment.communityComment.id,
                        QCommunityComment.communityComment.comment,
                        QCommunityComment.communityComment.member.memberId,
                        QCommunityComment.communityComment.member.name,
                        QCommunityComment.communityComment.createTime,
                        QCommunityComment.communityComment.community.id,
                        QCommunityComment.communityComment.community.title))
                .from(QCommunityComment.communityComment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QCommunityComment.communityComment)
                .fetchFirst();
        return new PageImpl<>(result, pageable, total);
    }
}
