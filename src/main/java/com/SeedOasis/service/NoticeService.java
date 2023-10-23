package com.SeedOasis.service;

import com.SeedOasis.dto.*;
import com.SeedOasis.entity.Notice;
import com.SeedOasis.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileService fileService;

    public List<String> imgPatternName(String imgTag) {
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(imgTag);
        List<String> src = new ArrayList<>();

        while (matcher.find()) {
            src.add(matcher.group(1));
        }

        return src;
    }

    public void saveNotice(NoticeFormDto noticeFormDto) {

        imgPatternName(noticeFormDto.getContent()).forEach(fileService::moveTempFile);

        Notice notice = Notice.builder()
                .title(noticeFormDto.getTitle())
                .content(noticeFormDto.getContent().replace("<img src=\"/files/temp", "<img src=\"/files"))
                .build();

        noticeRepository.save(notice);

    }

    public boolean modifyNotice(NoticeFormDto noticeFormDto) {

        List<String> imgName = imgPatternName(noticeFormDto.getContent()); //새로 저장하는 이미지
        Notice savedNotice = noticeRepository.findById(noticeFormDto.getNoticeId()).orElse(null);

        if (savedNotice == null)
            return false;

        imgName.forEach(e -> {
            if (e.contains("temp")) { //새로 추가한 이미지
                fileService.moveTempFile(e);
            }
        });

        savedNotice.setTitle(noticeFormDto.getTitle());
        savedNotice.setContent(noticeFormDto.getContent().replace("<img src=\"/files/temp", "<img src=\"/files"));
        return true;
    }

    public NoticeDtlDto getNoticeDtl(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice != null) {
            notice.setViews(notice.getViews() + 1);
            return NoticeDtlDto.builder()
                    .noticeId(notice.getNoticeId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .views(notice.getViews())
                    .createTime(notice.getCreateTime())
                    .build();
        }

        return null;
    }

    public NoticeFormDto getNoticeForm(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice != null) {
            return NoticeFormDto.builder()
                    .noticeId(notice.getNoticeId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .build();
        }

        return null;
    }

    public boolean delNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice != null) {
            noticeRepository.delete(notice);
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public Page<NoticePageDto> getNoticePage(int page, SearchDto noticeSearchDto) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Notice> pageNotice = noticeRepository.getNoticeListPage(pageRequest, noticeSearchDto);
        return pageNotice.map(e -> NoticePageDto.builder()
                .noticeId(e.getNoticeId())
                .title(e.getTitle())
                .createTime(e.getCreateTime())
                .views(e.getViews()).build());

    }

    public List<NewInfoDto> findTop5Notices() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Notice> notices = noticeRepository.findTop5Notices(pageRequest);
        List<NewInfoDto> newInfoDtoList = new ArrayList<>();
        notices.forEach(e -> {
            newInfoDtoList.add(NewInfoDto.builder()
                    .id(e.getNoticeId())
                    .title(e.getTitle())
                    .createTime(e.getCreateTime())
                    .views(e.getViews())
                    .build());
        });
        return newInfoDtoList;
    }
}
