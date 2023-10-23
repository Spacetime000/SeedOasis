package com.SeedOasis.service;

import com.SeedOasis.entity.Notice;
import com.SeedOasis.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @Test
    @DisplayName("테스트 생성")
    void saveNotice() {
        for (int i = 0; i < 200; i++) {
            Notice notice = Notice.builder()
                    .title("공지사항 테스트 " + i)
                    .content("공지사항 내용 테스트 " + i)
                    .build();

            noticeRepository.save(notice);
        }
    }
}