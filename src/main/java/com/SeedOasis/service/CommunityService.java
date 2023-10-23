package com.SeedOasis.service;

import com.SeedOasis.dto.*;
import com.SeedOasis.entity.Community;
import com.SeedOasis.entity.CommunityComment;
import com.SeedOasis.entity.Member;
import com.SeedOasis.repository.CommunityCommentRepository;
import com.SeedOasis.repository.CommunityRepository;
import com.SeedOasis.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityCommentRepository communityCommentRepository;

    public Community findById(Long id) {
        Optional<Community> optional = communityRepository.findById(id);
        return optional.isEmpty() ? null : optional.get();
    }

    public void createComment(Community community, String comment, String memberId) {
        Member member = memberRepository.findById(memberId).get();
        CommunityComment cc = new CommunityComment();
        cc.setMember(member);
        cc.setComment(comment);
        cc.setCommunity(community);
        communityCommentRepository.save(cc);
    /*
        return CommunityCommentDto.builder()
                .id(cc.getId())
                .profile(cc.getMember().getAvatar())
                .name(cc.getMember().getName())
                .createBy(cc.getMember().getMemberId())
                .comment(cc.getComment())
                .createTime(cc.getCreateTime())
                .build();
    */
    }

    public Long create(CommunityFormDto communityFormDto) {
        Community community = Community.builder()
                .title(communityFormDto.getTitle())
                .content(communityFormDto.getContent())
                .communityCategory(communityFormDto.getCommunityCategory())
                .build();

        return communityRepository.save(community).getId();
    }

    public CommunityDto readCommunity(Long id) {
        Optional<Community> optionalCommunity = communityRepository.findById(id);

        if (optionalCommunity.isPresent()) {
            Community community = optionalCommunity.get();
            Member member = memberRepository.findById(community.getCreateBy()).get();
            community.setViews(community.getViews() + 1);
            List<CommunityCommentDto> commentList = new ArrayList<>();
            community.getComments().stream().forEach(e -> {
                CommunityCommentDto commentDto = CommunityCommentDto.builder()
                        .id(e.getId())
                        .profile(e.getMember().getAvatar())
                        .name(e.getMember().getName())
                        .createBy(e.getMember().getMemberId())
                        .comment(e.getComment())
                        .createTime(e.getCreateTime())
                        .build();
                commentList.add(commentDto);
            });



            return CommunityDto.builder()
                    .id(community.getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .communityCategory(community.getCommunityCategory())
                    .createBy(community.getCreateBy())
                    .writer(member.getName())
                    .createTime(community.getCreateTime())
                    .views(community.getViews())
                    .profile(member.getAvatar())
                    .commentDtoList(commentList)
                    .build();
        }

        return null;
    }

    //
    @Transactional(readOnly = true)
    public Page<CommunityListDto> listPage(int page, CommunitySearchDto communitySearchDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Community> communities = communityRepository.communityListPage(pageRequest, communitySearchDto);
        return communities.map(c -> CommunityListDto.builder()
                .id(c.getId())
                .communityCategory(c.getCommunityCategory())
                .title(c.getTitle())
                .createBy(memberRepository.findById(c.getCreateBy()).get().getName())
                .createTime(c.getCreateTime())
                .views(c.getViews())
                .build());
    }

    @Transactional(readOnly = true)
    public Page<AdminCommunityListDto> listPage(int page, CommunitySearchDto communitySearchDto, SortDto sortDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return communityRepository.communityListPage(pageRequest, communitySearchDto, sortDto);
    }

    public void deleteCommunity(Long communityId) {
        communityRepository.findById(communityId).ifPresent(communityRepository::delete);
    }

    //글 제거
    public void deleteCommunity(String memberId, Long communityId) {
        /*Optional<Community> optional = communityRepository.findById(communityId);

        if (optional.isPresent()) {
            Community community = optional.get();
            if (memberId.equals(community.getCreateBy())) {
                communityRepository.delete(community);
            }
        }*/
        communityRepository.findById(communityId).ifPresent(e -> {
            if (memberId.equals(e.getCreateBy())) {
                communityRepository.delete(e);
            }
        });
    }

    //댓글 제거
    public void deleteComment(String memberId, Long commentId) {
        communityCommentRepository.findById(commentId).ifPresent(e -> {
            if (memberId.equals(e.getMember().getMemberId())) {
                communityCommentRepository.delete(e);
            }
        });
    }

    public CommunityFormDto readCommunityForm(Long communityId, String memberId) {
        Optional<Community> optionalCommunity = communityRepository.findById(communityId);
        if (optionalCommunity.isPresent()) {
            Community community = optionalCommunity.get();
            if (community.getCreateBy().equals(memberId)) {
                return CommunityFormDto.builder()
                        .title(community.getTitle())
                        .content(community.getContent())
                        .communityCategory(community.getCommunityCategory())
                        .build();
            }
        }

        return null;
    }

    public void modify(CommunityFormDto communityFormDto, Long communityId) {
        Optional<Community> optional = communityRepository.findById(communityId);

        if (optional.isPresent()) {
            Community community = optional.get();
            community.setCommunityCategory(communityFormDto.getCommunityCategory());
            community.setContent(communityFormDto.getContent());
            community.setTitle(communityFormDto.getTitle());
        }
    }

    public boolean modifyComment(CommunityCommentFormDto commentDto, String memberId) {
        Optional<CommunityComment> optionalComment = communityCommentRepository.findById(commentDto.getId());
        if (optionalComment.isPresent()) {
            CommunityComment communityComment = optionalComment.get();

            if (communityComment.getMember().getMemberId().equals(memberId)) {
                communityComment.setComment(commentDto.getComment());
                return true;
            }
        }
        return false;
    }

    public List<NewInfoDto> findTop5Communities() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Community> communityList = communityRepository.findTop5Community(pageRequest);
        List<NewInfoDto> newInfoDtoList = new ArrayList<>();
        communityList.forEach(e -> {
            newInfoDtoList.add(NewInfoDto.builder()
                    .id(e.getId())
                    .category(e.getCommunityCategory().getName())
                    .title(e.getTitle())
                    .writer(memberRepository.getName(e.getCreateBy()))
                    .createTime(e.getCreateTime())
                    .views(e.getViews())
                    .build());
        });

        return newInfoDtoList;
    }

    public List<AdminGraph> getCommunityLastWeekCount() {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        return communityRepository.getCommunityLastWeekCount(localDateTime.minusDays(6), localDateTime.plusDays(1));
    }

    public Long communityCount() {
        return communityRepository.count();
    }

    public Long getTodayCount() {
        return communityRepository.getTodayCount();
    }

    public Long commentCount() {
        return communityCommentRepository.count();
    }

    public Page<AdminCommentListDto> commentListPage(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return communityCommentRepository.commentListPage(pageRequest);
    }

    public void delComment(Long id) {
        communityCommentRepository.findById(id).ifPresent(communityCommentRepository::delete);
    }

}
