package com.SeedOasis.controller;

import com.SeedOasis.dto.TestPageDto;
import com.SeedOasis.service.TestLikeService;
import com.SeedOasis.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("management")
@RequiredArgsConstructor
public class ManagementController {

    private final static int MAX_PAGE = 5;

    private final TestService testService;
    private final TestLikeService testLikeService;

    @GetMapping("tests")
    public String testManagement(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Page<TestPageDto> pages = testService.findByMemberId(authentication.getName(), page);
        model.addAttribute("pages", pages);
        model.addAttribute("maxPage", MAX_PAGE);
        return "tests/test_management";
    }

    @PatchMapping("tests/{testId}")
    public ResponseEntity changeTestEnable(@PathVariable Long testId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> map = testService.changeEnable(testId, authentication.getName());

        if (map.containsKey("success")) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(map.get("message"));
        }
    }

    @GetMapping("tests/like")
    public String testLike(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Page<TestPageDto> pages = testLikeService.testLikePage(authentication.getName(), page);
        model.addAttribute("pages", pages);
        model.addAttribute("maxPage", MAX_PAGE);
        return "tests/test_like";  //진행중
    }
}
