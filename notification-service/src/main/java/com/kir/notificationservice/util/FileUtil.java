package com.kir.notificationservice.util;

import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtil {
    public static void saveFile(String url, String content) {
        try {
            Path path = Paths.get(url);
            Files.createDirectories(path.getParent());
            Files.write(path, content.getBytes());
            log.info("File saved successfully.");
        } catch (IOException e) {
            throw new AppException(ErrorCode.CAN_NOT_SAVE_FILE);
        }
    }

    public static void deleteFile(String url) {
        try{
            File file = new File(url);
            if (file.exists()) {
                file.delete();
                log.info("File deleted successfully.");
            } else {
                log.info("File not found.");
            }
        }catch (Exception e){
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

    }
}
