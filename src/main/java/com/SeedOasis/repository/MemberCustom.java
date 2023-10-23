package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminMemberListDto;
import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.dto.SortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustom {
    Page<AdminMemberListDto> memberPage(Pageable pageable, SortDto sortDto);
}
