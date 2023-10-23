package com.SeedOasis.controller;

import com.SeedOasis.dto.JoinDto;
import com.SeedOasis.dto.NewInfoDto;
import com.SeedOasis.entity.Member;
import com.SeedOasis.service.CommunityService;
import com.SeedOasis.service.MemberService;
import com.SeedOasis.service.NoticeService;
import com.SeedOasis.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final TestService testService;
    private final NoticeService noticeService;
    private final CommunityService communityService;

    @GetMapping("/")
    public String home(Model model) {
        List<NewInfoDto> tests = testService.findTop5Tests();
        List<NewInfoDto> notices = noticeService.findTop5Notices();
        List<NewInfoDto> communities = communityService.findTop5Communities();

        model.addAttribute("tests", tests);
        model.addAttribute("notices", notices);
        model.addAttribute("communities", communities);
        return "index";
    }

    //회원가입 페이지
    @GetMapping("signup")
    public String showSignupPage(Model model) {
        model.addAttribute("joinDto", new JoinDto());
        return "member/signup";
    }

    //회원가입 요청
    @PostMapping("signup")
    public String processSignup(@Valid JoinDto joinDto, BindingResult bindingResult) {

        if (memberService.saveMember(joinDto, bindingResult))
            return "redirect:/login";
        else {
            return "member/signup";
        }

    }

    @GetMapping("profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> optionalMember = memberService.findById(authentication.getName());
        optionalMember.ifPresent(m -> model.addAttribute("profile", m.getAvatar()));
        return "member/profile";
    }

    @PostMapping("profile")
    public String saveProfile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        memberService.saveProfile(authentication.getName(), multipartFile);
        return "redirect:/profile";
    }

    @GetMapping("login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("login/err")
    public String loginErrorPage() {
        return "member/loginErr";
    }

}
