package com.SeedOasis.controller;

import com.SeedOasis.constant.CommunityCategory;
import com.SeedOasis.constant.Direction;
import com.SeedOasis.constant.TestCategory;
import com.SeedOasis.dto.*;
import com.SeedOasis.service.CommunityService;
import com.SeedOasis.service.MemberService;
import com.SeedOasis.service.TestService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final CommunityService communityService;
    private final TestService testService;

    @ModelAttribute("communityCategories")
    public CommunityCategory[] communityCategories() {
        return CommunityCategory.values();
    }

    @ModelAttribute("testCategories")
    public TestCategory[] testCategories() {
        return TestCategory.values();
    }

    @GetMapping
    public String main(Model model) {
        List<AdminGraph> memberLastWeekCount = memberService.getMemberLastWeekCount();
        List<AdminGraph> communityLastWeekCount = communityService.getCommunityLastWeekCount();
        List<AdminGraph> testLastWeekCount = testService.getTestLastWeekCount();

        LocalDate now = LocalDate.now().minusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        List<String> dateList = new ArrayList<>();
        List<Long> memberCountList = new ArrayList<>();
        List<Long> communityCountList = new ArrayList<>();
        List<Long> testCountList = new ArrayList<>();

        for (int i = 0, m = 0, c = 0, t = 0; i < 7; i++) {
            LocalDate localDate = now.plusDays(i);
            dateList.add(localDate.format(formatter));

            memberCountList.add(memberLastWeekCount.size() > m && memberLastWeekCount.get(m).getTime().equals(localDate) ? memberLastWeekCount.get(m++).getCount() : 0L);
            communityCountList.add(communityLastWeekCount.size() > c && communityLastWeekCount.get(c).getTime().equals(localDate) ? communityLastWeekCount.get(c++).getCount() : 0L);
            testCountList.add(testLastWeekCount.size() > t && testLastWeekCount.get(t).getTime().equals(localDate) ? testLastWeekCount.get(t++).getCount() : 0L);
        }

        model.addAttribute("dateList", dateList);
        model.addAttribute("memberCountList", memberCountList);
        model.addAttribute("communityCountList", communityCountList);
        model.addAttribute("testCountList", testCountList);
        model.addAttribute("memberCount", memberService.count());
        model.addAttribute("communityCount", communityService.communityCount());
        model.addAttribute("commentCount", communityService.commentCount());
        model.addAttribute("testCount", testService.count());
        model.addAttribute("todayMember", memberService.getTodayCount());
        model.addAttribute("todayCommunity", communityService.getTodayCount());
        model.addAttribute("todayTest", testService.getTodayCount());
        return "admin/main";
    }

    @GetMapping("community")
    public String communityManagement(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "category", required = false) String category,
                                      @RequestParam(value = "query", required = false) String query,
                                      @RequestParam(value = "sort", defaultValue = "id") String sort,
                                      @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                      Model model) {

        CommunitySearchDto communitySearchDto = new CommunitySearchDto();
        communitySearchDto.setCommunityCategory(category == null || category.equals("all") ? null : CommunityCategory.valueOf(category));
        communitySearchDto.setSearchQuery(query == null ? "" : query);

        SortDto sortDto = new SortDto(sort, Direction.valueOf(direction));
        model.addAttribute("sort", sortDto);

        Page<AdminCommunityListDto> pages = communityService.listPage(page, communitySearchDto, sortDto);

        if (communitySearchDto.getCommunityCategory() != null)
            model.addAttribute("selectedCategory", communitySearchDto.getCommunityCategory());
        model.addAttribute("pages", pages);

        return "admin/managements/community";
    }

    @PostMapping("community")
    public ResponseEntity delCommunity(@RequestBody List<Long> id) {
        id.forEach(communityService::deleteCommunity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("tests")
    public String testManagement(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "sort", defaultValue = "id") String sort,
                                 @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                 @RequestParam(value = "searchBy", required = false) String searchBy,
                                 Model model) {

        SortDto sortDto = new SortDto(sort, Direction.valueOf(direction));
        model.addAttribute("sort", sortDto);

        TestSearchDto testSearchDto = new TestSearchDto();
        testSearchDto.setCategory(category == null || category.equals("all") ? null : TestCategory.valueOf(category));
        testSearchDto.setSearchQuery(query == null ? "" : query);
        testSearchDto.setSearchBy(searchBy == null ? "" : searchBy);

        Page<AdminTestListDto> pages = testService.listPage(page, testSearchDto, sortDto);
        model.addAttribute("pages", pages);

        if (testSearchDto.getCategory() != null)
            model.addAttribute("selectedCategory", testSearchDto.getCategory());

        return "admin/managements/test";
    }

    @PostMapping("tests")
    public ResponseEntity delTest(@RequestBody List<Long> id) {
        id.forEach(testService::deleteTest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("members")
    public String memberManagement(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "sort", defaultValue = "id") String sort,
                                   @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                   Model model) {

        SortDto sortDto = new SortDto(sort, Direction.valueOf(direction));
        model.addAttribute("sort", sortDto);

        Page<AdminMemberListDto> pages = memberService.listPage(page, sortDto);
        model.addAttribute("pages", pages);

        return "admin/managements/member";
    }

    @GetMapping("comments")
    public String commentManagement(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Page<AdminCommentListDto> pages = communityService.commentListPage(page);
        model.addAttribute("pages", pages);
        return "admin/managements/comment";
    }

    @PostMapping("comments")
    public ResponseEntity delComment(@RequestBody List<Long> id) {
        id.forEach(communityService::delComment);
        return ResponseEntity.ok().build();
    }
}
