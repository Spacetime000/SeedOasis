package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminCommentListDto;
import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.dto.SortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityCommentCustom {

    Page<AdminCommentListDto> commentListPage(Pageable pageable);
}
