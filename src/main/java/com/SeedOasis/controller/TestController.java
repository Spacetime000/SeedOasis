package com.SeedOasis.controller;

import com.SeedOasis.constant.TestCategory;
import com.SeedOasis.dto.*;
import com.SeedOasis.entity.Test;
import com.SeedOasis.service.FileService;
import com.SeedOasis.service.MemberService;
import com.SeedOasis.service.TestLikeService;
import com.SeedOasis.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("tests")
@RequiredArgsConstructor
public class TestController {

    private final static int MAX_PAGE = 5;

    private final TestService testService;
    private final FileService fileService;
    private final MemberService memberService;
    private final TestLikeService testLikeService;
    private TestCategory[] testCategories;

    //재사용
    @PostConstruct
    public void initTestCategory() {
        testCategories = TestCategory.values();
    }

    @ModelAttribute("testCategories")
    public TestCategory[] testCategories() {
        return testCategories;
    }

    @GetMapping("")
    public String testList(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "option", required = false) String option,
                           @RequestParam(value = "query", required = false) String query,
                           Model model) {
        model.addAttribute("testDto", new TestDto());

        TestSearchDto testSearchDto = new TestSearchDto();

        if (query != null && option != null && category != null) {
            if (!category.equals("all")) testSearchDto.setCategory(TestCategory.valueOf(category));
            testSearchDto.setSearchBy(option);
            testSearchDto.setSearchQuery(query);
        }

        Page<TestPageDto> testPage = testService.getTestListPage(page, testSearchDto);
        model.addAttribute("pages", testPage);
        model.addAttribute("maxPage", MAX_PAGE);

        return "tests/test_list";
    }

    @PostMapping("new")
    public String newTest(@Valid TestDto testDto, BindingResult bindingResult, Model model) { //아직

        StringBuilder sb = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
            model.addAttribute("error", sb);
            return "tests/test_list";
        }
        Long testId = testService.newTest(testDto);

        return "redirect:/tests/edit/" + testId;
    }

    @GetMapping("{testId}")
    public String readTest(@PathVariable Long testId, Model model, RedirectAttributes re) throws JsonProcessingException {
        /**
         * MEMO
         * 공개여부 넣을 것.
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Test> optionalTest = testService.findById(testId);

        if (optionalTest.isPresent()) {
            Test test = optionalTest.get();
            //작성자 O, 공개 O -> O
            //작성자 O, 공개 X -> O
            //작성자 X, 공개 O -> O
            //작성자 X, 공개 X -> X
            if (!(test.getCreateBy().equals(authentication.getName()) || test.getEnabled())) {
                re.addFlashAttribute("error", "비공개 처리된 문제집입니다.");
                return "redirect:/tests";
            }

            model.addAttribute("test", test);

            if (test.getContent().length() != 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<TestFormDto> testFormDtoList = Arrays.asList(objectMapper.readValue(test.getContent(), TestFormDto[].class));
                model.addAttribute("testFormList", testFormDtoList);
            }
            if (testLikeService.findById(authentication.getName(), testId).isPresent()) {
                model.addAttribute("isLike", true);
            } else {
                model.addAttribute("isLike", false);
            }
            testService.viewPlus(test.getTestId());
            return "tests/test_dtl";

        }
        re.addFlashAttribute("error", "존재하지 않는 문제집입니다.");
        return "redirect:/tests";
    }

    //edit
    @GetMapping("edit/{testId}")
    public String editTest(@PathVariable Long testId, Model model, Principal principal) throws JsonProcessingException {
        Optional<Test> optionalTest = testService.findById(testId);

        if (optionalTest.isPresent()) { //존재 여부
            //form 데이터 고민
            Test test = optionalTest.get();
            if (principal.getName().equals(test.getCreateBy())) { //수정 권한
                ObjectMapper objectMapper = new ObjectMapper();
                model.addAttribute("test", test);

                if (test.getContent().length() != 0) {
                    List<TestFormDto> testFormDtoList = Arrays.asList(objectMapper.readValue(test.getContent(), TestFormDto[].class));
                    model.addAttribute("testFormList", testFormDtoList);
                }

            }

            return "tests/test_form";
        }


        //권한 X, 존재 X
        return "redirect:/tests";
    }

    @PostMapping("edit/{testId}")
    public String saveTest(TestPostDto testPostDto, @PathVariable Long testId, Principal principal) throws JsonProcessingException {

        try {
            Optional<Test> optionalTest = testService.findById(testId);
            if (optionalTest.isPresent()) {
                Test test = optionalTest.get();

                //작성자와 수정자가 일치 여부
                if (principal.getName().equals(test.getCreateBy())) {
                    List<Map<String, String>> content = new ArrayList<>();

                    if (testPostDto.getQuestion() != null) {
                        for (int i = 0; i < testPostDto.getQuestion().size(); i++) {
                            Map<String, String> map = new HashMap<>();
                            map.put("question", testPostDto.getQuestion().get(i));
                            map.put("answers", testPostDto.getAnswers().get(i));

                            //multipart 는 not null 이면 이미지 변경, null 이면 기존 이미지 사용하거나 이미지 제거.
                            MultipartFile multipartFile = testPostDto.getImg().get(i);

                            if (!multipartFile.isEmpty()) { //이미지 변경
                                map.put("img", fileService.uploadFile(multipartFile, "test"));
                            } else { //이미지 제거 또는 기존 이미지 사용
                                if (testPostDto.getOriginImg().get(i).length() == 0) { //이미지 제거
                                    map.put("img", "");
                                } else { //기존 이미지 사용
                                    map.put("img", testPostDto.getOriginImg().get(i));
                                }
                            }

                            content.add(map);
                        }
                        ObjectMapper objectMapper = new ObjectMapper();
                        testService.saveTestContent(testId, objectMapper.writeValueAsString(content));
                        return "redirect:/tests/" + testId;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/tests";
    }

    @GetMapping("{testId}/like")
    @ResponseBody
    public String like(@PathVariable Long testId) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        if (testLikeService.insertLike(authentication.getName(), testId)) { //정상 처리
            map.put("success", "true");
            return objectMapper.writeValueAsString(map);
        }

        map.put("success", "false");
        return objectMapper.writeValueAsString(map);
    }

    @DeleteMapping("{testId}/like")
    @ResponseBody
    public String likeCancle(@PathVariable Long testId) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        if (testLikeService.deleteLike(authentication.getName(), testId)) { //정상 처리
            map.put("success", "true");
            return objectMapper.writeValueAsString(map);
        }

        map.put("success", "false");
        return objectMapper.writeValueAsString(map);
    }

    @DeleteMapping("{testId}")
    @ResponseBody
    public ResponseEntity deleteTest(@PathVariable Long testId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (testService.deleteTest(testId, authentication.getName())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
