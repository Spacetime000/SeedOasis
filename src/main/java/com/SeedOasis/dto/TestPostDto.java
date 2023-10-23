package com.SeedOasis.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TestPostDto {

//    private Long testId;

    private List<String> question;
    private List<String> answers;
    private List<String> originImg;
    private List<MultipartFile> img;

}
