package com.SeedOasis.controller;

import com.SeedOasis.dto.NoticeDtlDto;
import com.SeedOasis.dto.NoticeFormDto;
import com.SeedOasis.dto.NoticePageDto;
import com.SeedOasis.dto.SearchDto;
import com.SeedOasis.service.FileService;
import com.SeedOasis.service.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController {

    private final static int MAX_PAGE = 5;

    private final FileService fileService;
    private final NoticeService noticeService;

    @GetMapping("")
    public String noticeList(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "search_type", required = false) String searchBy,
                             @RequestParam(value = "search_query", required = false) String searchQuery,
                             Model model) {

        SearchDto noticeSearchDto = new SearchDto();

        if (searchBy != null && searchQuery != null) {
            noticeSearchDto.setSearchBy(searchBy);
            noticeSearchDto.setSearchQuery(searchQuery);
        }

        Page<NoticePageDto> noticePage = noticeService.getNoticePage(page, noticeSearchDto);
        model.addAttribute("pages", noticePage);
        model.addAttribute("maxPage", MAX_PAGE);
        return "notices/notice_list";
    }

    @GetMapping("new")
    public String newNotice(Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "notices/notice_form";
    }

    @PostMapping("new")
    public String saveNotice(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors())
            return "notices/notice_new";

        noticeService.saveNotice(noticeFormDto);

        return "redirect:/notice";
    }

    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam(value = "upload") MultipartFile multipartFile) {
        String savedImgFile;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        try {
            savedImgFile = fileService.uploadFile(multipartFile, "temp/notice");
            map.put("url", savedImgFile);
            return mapper.writeValueAsString(map);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{\"error\": {\"message\" : \"이미지 업로드 실패\"}}";
    }

    @GetMapping("{noticeId}")
    public String notice(@PathVariable Long noticeId, Model model) {
        NoticeDtlDto noticeDtlDto = noticeService.getNoticeDtl(noticeId);

        if (noticeDtlDto != null) {
            model.addAttribute("notice", noticeDtlDto);
            return "notices/notice_dtl";
        }

        model.addAttribute("error", "해당 공지사항 게시글이 존재하지 않습니다.");
        return "redirect:/notice";
    }

    @GetMapping("/{noticeId}/modify")
    public String modifyNotice(@PathVariable Long noticeId, Model model) {
        NoticeFormDto noticeFormDto = noticeService.getNoticeForm(noticeId);

        if (noticeFormDto != null) {
            model.addAttribute("noticeFormDto", noticeFormDto);
            return "notices/notice_form";
        }

        model.addAttribute("error", "해당 공지사항 게시글이 존재하지 않습니다.");
        return "redirect:/notice";
    }

    @PostMapping("{noticeId}/modify")
    public String saveModifyNotice(@Valid NoticeFormDto noticeFormDto, @PathVariable Long noticeId) {
        boolean result = noticeService.modifyNotice(noticeFormDto);

        if (result) {
            return "redirect:/notice/" + noticeId;
        } else
            return "redirect:/notice";
    }

    @DeleteMapping("{noticeId}")
    @ResponseBody
    public String deleteNotice(@PathVariable Long noticeId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        if (noticeService.delNotice(noticeId)) {
            map.put("success", "true");
            map.put("message", "해당 공지 글이 삭제되었습니다.");
            return objectMapper.writeValueAsString(map);
        }

        map.put("success", "false");
        map.put("message", "이미 삭제된 글입니다.");
        return objectMapper.writeValueAsString(map);
    }
}
