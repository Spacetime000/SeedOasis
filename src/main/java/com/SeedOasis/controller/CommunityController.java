package com.SeedOasis.controller;

import com.SeedOasis.constant.CommunityCategory;
import com.SeedOasis.dto.*;
import com.SeedOasis.entity.Community;
import com.SeedOasis.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @ModelAttribute("communityCategories")
    public CommunityCategory[] communityCategories() {
        return CommunityCategory.values();
    }

    @GetMapping()
    public String list(@RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "searchBy", required = false) String searchBy,
                       @RequestParam(value = "query", required = false) String query,
                       Model model) {

        CommunitySearchDto communitySearchDto = new CommunitySearchDto();

        if (query != null && searchBy != null && category != null) { //검색 기능
            if (!category.equals("all")) communitySearchDto.setCommunityCategory(CommunityCategory.valueOf(category));
            communitySearchDto.setSearchBy(searchBy);
            communitySearchDto.setSearchQuery(query);
        }

        Page<CommunityListDto> communityList = communityService.listPage(page, communitySearchDto);
        model.addAttribute("pages", communityList);
        model.addAttribute("maxPage", 5);
        return "community/community_list";
    }

    @GetMapping("new")
    public String create(Model model) {
        model.addAttribute("community", new CommunityFormDto());
        return "community/community_create";
    }

    @PostMapping("new")
    public String save(@Valid @ModelAttribute("community") CommunityFormDto communityFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "community/community_create";
        }

        Long id = communityService.create(communityFormDto);
        
        return "redirect:/community/" + id;
    }

    @GetMapping("{id}")
    public String community(@PathVariable Long id, Model model, RedirectAttributes ra) {
        CommunityDto communityDto = communityService.readCommunity(id);

        if (communityDto == null) {
            ra.addFlashAttribute("error", "해당 게시글이 존재하지 않습니다.");
            return "redirect:/community";
        }

        model.addAttribute("community", communityDto);
        return "community/community_dtl";
    }

    @PostMapping("comment")
    public ResponseEntity newComment(@RequestBody CommunityCommentFormDto communityCommentFormDto) {

        Community community = communityService.findById(communityCommentFormDto.getId());
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (community == null)
            return ResponseEntity.badRequest().body("게시글이 존재하지 않습니다.");

        communityService.createComment(community, communityCommentFormDto.getComment(), name);
//        CommunityCommentDto comment = communityService.createComment(community, communityCommentFormDto.getComment(), name);
//        return ResponseEntity.ok().body(comment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public void delCommunity(@PathVariable Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        communityService.deleteCommunity(name, id);
//        return ResponseEntity.ok().build();
    }

    @DeleteMapping("comment/{id}")
    public void delComment(@PathVariable Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        communityService.deleteComment(name, id);
    }

    @PatchMapping("comment")
    public ResponseEntity modify(@RequestBody CommunityCommentFormDto communityCommentFormDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        if (communityService.modifyComment(communityCommentFormDto, id))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("{id}/modify")
    public String modifyCommunity(@PathVariable Long id, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        CommunityFormDto communityFormDto = communityService.readCommunityForm(id, name);

        if (communityFormDto != null) {
            model.addAttribute("community", communityFormDto);
            return "community/community_modify";
        }

        return "redirect:/community";
    }

    @PostMapping("{id}/modify")
    public String modifyCommunity(@Valid @ModelAttribute("community") CommunityFormDto communityFormDto, BindingResult bindingResult, @PathVariable Long id, Model model) {

        if (bindingResult.hasErrors()) {
            return "community/community_modify";
        }

        communityService.modify(communityFormDto, id);
        return "redirect:/community/" + id;
    }

}
