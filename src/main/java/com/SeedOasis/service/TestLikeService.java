package com.SeedOasis.service;

import com.SeedOasis.dto.TestPageDto;
import com.SeedOasis.entity.Member;
import com.SeedOasis.entity.Test;
import com.SeedOasis.entity.TestLike;
import com.SeedOasis.entity.TestLikeId;
import com.SeedOasis.repository.MemberRepository;
import com.SeedOasis.repository.TestLikeRepository;
import com.SeedOasis.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TestLikeService {

    private final TestLikeRepository testLikeRepository;
    private final MemberService memberService;
    private final TestService testService;
    private final TestRepository testRepository;
    private final MemberRepository memberRepository;

    public boolean insertLike(String memberId, Long testId) {
        Optional<Member> optionalMember = memberService.findById(memberId);
        Optional<Test> optionalTest = testService.findById(testId);

        if (optionalMember.isPresent() && optionalTest.isPresent()) {
            Member member = optionalMember.get();
            Test test = optionalTest.get();
            TestLike testLike = new TestLike();
            testLike.setMember(member);
            testLike.setTest(test);
            testLikeRepository.save(testLike);
            return true;
        }
        return false;
    }

    public boolean deleteLike(String memberId, Long testId) {
        TestLikeId testLikeId = new TestLikeId();
        testLikeId.setTest(testId);
        testLikeId.setMember(memberId);
        Optional<TestLike> optionalTestLike = testLikeRepository.findById(testLikeId);

        if (optionalTestLike.isPresent()) {
            testLikeRepository.delete(optionalTestLike.get());
            return true;
        }

        return false;

        /*Optional<Member> optionalMember = memberService.findById(memberId);
        Optional<Test> optionalTest = testService.findById(testId);

        if (optionalMember.isPresent() && optionalTest.isPresent()) {
//            Member member = optionalMember.get();
//            Test test = optionalTest.get();
            TestLikeId testLikeId = new TestLikeId();
            testLikeId.setTest(testId);
//            testLikeId.setTest(test);
            testLikeId.setMember(memberId);
//            testLikeId.setMember(member);

            Optional<TestLike> optionalTestLike = testLikeRepository.findById(testLikeId);
            optionalTestLike.ifPresent(testLikeRepository::delete);

            return true;
        }
        return false;*/
    }

    @Transactional(readOnly = true)
    public Optional<TestLike> findById(String memberId, Long testId) {
        TestLikeId testLikeId = new TestLikeId();
        testLikeId.setTest(testId);
        testLikeId.setMember(memberId);

        return testLikeRepository.findById(testLikeId);
    }

    @Transactional(readOnly = true)
    public Page<TestPageDto> testLikePage(String memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Test> tests = testRepository.testLikePage(memberId, pageRequest);
        return tests.map(testPage -> TestPageDto.builder()
                .id(testPage.getTestId())
                .title(testPage.getTitle())
                .category(testPage.getTestCategory())
                .views(testPage.getViews())
                .like(testPage.getTestLikes().size())
                .createBy(memberRepository.getName(testPage.getCreateBy()))
                .createTime(testPage.getCreateTime())
                .build());
    }

}
