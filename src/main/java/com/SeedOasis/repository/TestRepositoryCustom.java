package com.SeedOasis.repository;

import com.SeedOasis.dto.AdminTestListDto;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.dto.TestSearchDto;
import com.SeedOasis.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestRepositoryCustom {

    Page<Test> getTestListPage(Pageable pageable, TestSearchDto testSearchDto);

    Page<AdminTestListDto> testListPage(Pageable pageable, TestSearchDto testSearchDto, SortDto sortDto);

}
