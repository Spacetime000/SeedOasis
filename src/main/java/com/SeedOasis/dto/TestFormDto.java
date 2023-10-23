package com.SeedOasis.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestFormDto {
    private String question;
    private String answers;
    private String img;
}
