package com.SeedOasis.service;

import com.SeedOasis.dto.*;
import com.SeedOasis.entity.Member;
import com.SeedOasis.entity.Test;
import com.SeedOasis.repository.MemberRepository;
import com.SeedOasis.repository.TestRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final MemberRepository memberRepository;

    public boolean deleteTest(Long testId, String memberId) {
        Optional<Test> optionalTest = testRepository.findById(testId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalTest.isPresent() && optionalMember.isPresent()) {
            if (optionalTest.get().getCreateBy().equals(optionalMember.get().getMemberId())) {
                testRepository.delete(optionalTest.get());
                return true;
            }
        }
        return false;
    }

    //문제집 생성
    public Long newTest(TestDto testDto) {
        Test test = new Test();
        test.setTitle(testDto.getTitle());
        test.setTestCategory(testDto.getTestCategory());
        test.setContent("");

        testRepository.save(test);

        return test.getTestId();
    }

//    @Transactional(readOnly = true)
//    public Test readTest(Long testId) {
//        return testRepository.findById(testId).orElse(null);
//    }

    @Transactional(readOnly = true)
    public Optional<Test> findById(Long testId) {
        return testRepository.findById(testId);
    }

    public void viewPlus(Long testId) {
        testRepository.findById(testId).ifPresent(t -> {
            t.setViews(t.getViews() + 1);
        });
    }


    //작성자 ID
/*    @Transactional(readOnly = true)
    public String findCreateByMemberId(Long testId) {
        Test test = testRepository.findById(testId).orElse(null);
        return test != null ? test.getCreateBy().getMemberId() : null;
    }*/

    public void saveTestContent(Long testId, String content) {
        testRepository.findById(testId).ifPresent(test -> {
            test.setContent(content);
        });
    }

    public Page<TestPageDto> getTestListPage(int page, TestSearchDto testSearchDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Test> pageTest = testRepository.getTestListPage(pageRequest, testSearchDto);
        return pageTest.map(e -> TestPageDto.builder()
                .id(e.getTestId())
                .title(e.getTitle())
                .category(e.getTestCategory())
                .views(e.getViews())
                .like(e.getTestLikes().size())
                .createTime(e.getCreateTime())
                .createBy(memberRepository.getName(e.getCreateBy()))
                .build());
    }

    public Page<TestPageDto> findByMemberId(String memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Test> testPages = testRepository.findByCreateBy(memberId, pageRequest);
        return testPages.map(testPage -> TestPageDto.builder()
                .id(testPage.getTestId())
                .title(testPage.getTitle())
                .category(testPage.getTestCategory())
                .views(testPage.getViews())
                .like(testPage.getTestLikes().size())
                .createTime(testPage.getCreateTime())
                .enabled(testPage.getEnabled())
                .build());
    }

    public Map<String, String> changeEnable(Long testId, String memberId) {
        Map<String, String> map = new HashMap<>();
        Optional<Test> optionalTest = testRepository.findById(testId);

        if (optionalTest.isPresent()) {
            if (optionalTest.get().getCreateBy().equals(memberId)) {
                //변경
                Test test = optionalTest.get();
                test.setEnabled(!test.getEnabled());
                map.put("success", "true");
            } else {
                map.put("message", "변경 권한이 없습니다.");
                return map;
            }
        } else {
            map.put("message", "해당 문제집이 존재하지 않습니다.");
            return map;
        }

        map.put("message", "에러가 발생하였습니다.");
        return map;
    }

    public List<NewInfoDto> findTop5Tests() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Test> testList = testRepository.findTop5Tests(pageRequest);
        List<NewInfoDto> newInfoDtoList = new ArrayList<>();
        testList.forEach(e -> {
            newInfoDtoList.add(NewInfoDto.builder()
                    .id(e.getTestId())
                    .category(e.getTestCategory().getCategory())
                    .title(e.getTitle())
                    .writer(memberRepository.getName(e.getCreateBy()))
                    .createTime(e.getCreateTime())
                    .views(e.getViews())
                    .build());
        });
        return newInfoDtoList;
    }

    public List<AdminGraph> getTestLastWeekCount() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return testRepository.getTestLastWeekCount(localDateTime.minusDays(6), localDateTime.plusDays(1));
    }

    public Long count() {
        return testRepository.count();
    }

    public Long getTodayCount() {
        return testRepository.getTodayCount();
    }

    public Page<AdminTestListDto> listPage(int page, TestSearchDto testSearchDto, SortDto sortDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return testRepository.testListPage(pageRequest, testSearchDto, sortDto);
    }

    public void deleteTest(Long testId) {
        testRepository.findById(testId).ifPresent(testRepository::delete);
    }
}
