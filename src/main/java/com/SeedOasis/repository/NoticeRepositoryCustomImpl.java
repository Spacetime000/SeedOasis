package com.SeedOasis.repository;

import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.entity.Notice;
import com.SeedOasis.entity.QNotice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public NoticeRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QNotice.notice.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return QNotice.notice.content.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Notice> getNoticeListPage(Pageable pageable, SearchDto noticeSearchDto) {
        BooleanExpression searchPredicate = searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery());

        List<Notice> result = jpaQueryFactory
                .selectFrom(QNotice.notice)
                .where(searchPredicate)
                .orderBy(QNotice.notice.createTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QNotice.notice)
                .where(searchPredicate)
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }
}
