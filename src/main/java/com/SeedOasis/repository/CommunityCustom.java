package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminCommunityListDto;
import com.SeedOasis.dto.CommunityDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.dto.CommunitySearchDto;
import com.SeedOasis.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityCustom {

    Page<Community> communityListPage(Pageable pageable, CommunitySearchDto communitySearchDto);
    Page<AdminCommunityListDto> communityListPage(Pageable pageable, CommunitySearchDto communitySearchDto, SortDto sortDto);

}
