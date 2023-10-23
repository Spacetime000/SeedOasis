package com.SeedOasis.service;

import com.SeedOasis.config.auth.PrincipalDetails;
import com.SeedOasis.dto.AdminMemberListDto;
import com.SeedOasis.dto.JoinDto;
import com.SeedOasis.dto.AdminGraph;
import com.SeedOasis.dto.SortDto;
import com.SeedOasis.entity.Member;
import com.SeedOasis.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    //회원가입
    public boolean saveMember(JoinDto joinDto, BindingResult bindingResult) {

        boolean isDuplicate = false;

        if (bindingResult.hasErrors())
            return false;

        //중복 검사
        if (memberRepository.existsByMemberId(joinDto.getMemberId())) {
            bindingResult.rejectValue("memberId", "duplicate", null, "사용중인 아이디입니다.");
            isDuplicate = true;
        }
        if (memberRepository.existsByEmail(joinDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", null, "이미 가입된 이메일입니다.");
            isDuplicate = true;
        }
        if (memberRepository.existsByName(joinDto.getName())) {
            bindingResult.rejectValue("name", "duplicate", null, "사용중인 닉네임입니다.");
            isDuplicate = true;
        }

        if (!isDuplicate) { //중복이 없으면 회원가입 진행.
            Member member = Member.builder()
                    .memberId(joinDto.getMemberId())
                    .email(joinDto.getEmail())
                    .password(passwordEncoder.encode(joinDto.getPassword()))
                    .name(joinDto.getName())
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);
            return true;
        }

        return false;

    }

    public void saveProfile(String memberId, MultipartFile multipartFile) throws IOException {
        String imgSrc = fileService.uploadFile(multipartFile, "profile");
        memberRepository.findById(memberId).ifPresent(m -> {
            m.setAvatar(imgSrc);
            PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            principalDetails.getMember().setAvatar(imgSrc);
        });
    }

    public List<AdminGraph> getMemberLastWeekCount() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return memberRepository.getMemberLastWeekCount(localDateTime.minusDays(6), localDateTime.plusDays(1));
    }

    public Long count() {
        return memberRepository.count();
    }

    public Long getTodayCount() {
        return memberRepository.getTodayCount();
    }

    public Page<AdminMemberListDto> listPage(int page, SortDto sortDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return memberRepository.memberPage(pageRequest, sortDto);
    }

}
