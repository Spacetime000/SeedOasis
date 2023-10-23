package com.SeedOasis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.url}")
    private String location; //업로드할 실제 경로

    /**
     * 파일 업로드
     * @param multipartFile
     * @param savePath - 저장 DIR NAME(e.g. /temp/test.txt -> temp)
     * @return "/files/" + savePath + "/" +  uuid + . + ext
     * @throws IOException
     */
    public String uploadFile(MultipartFile multipartFile, String savePath) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String oriFileName = multipartFile.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(oriFileName);
        String destFileName = uuid + "." + ext;
        File destFile = new File(location + savePath + "/" + destFileName);
        destFile.getParentFile().mkdirs();
        multipartFile.transferTo(destFile);
        return "/files/" + savePath + "/" + destFileName;
    }

    public void moveTempFile(String path) {
        String replace = path.replaceFirst("/files/", location);
        Path oldPath = Paths.get(replace);
        Path newPath = Paths.get(replace.replaceFirst("/temp", ""));

        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return path.replaceFirst("/temp", "");
    }


}
