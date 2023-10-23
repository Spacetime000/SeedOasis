package com.SeedOasis.repository;

import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.dto.TestPageDto;
import com.SeedOasis.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> getNoticeListPage(Pageable pageable, SearchDto noticeSearchDto);
}
