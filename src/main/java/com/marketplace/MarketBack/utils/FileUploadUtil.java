package com.marketplace.MarketBack.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {
    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bpm))$)";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String FILE_NAME_FORMAT = "%s_%s_%s";

    public static boolean isAllowedExtentsion(final String fileName, final String pattern){
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

//    public static void assertAllowed(MultipartFile file, String pattern){
//        final long size = file.getSize();
//
//        if(size > MAX_FILE_SIZE){
//            throw new RuntimeException();
//        }
//
//        final String fileName = file.getOriginalFilename();
//        final String extension = Filename.getExtension(fileName);
//
//        if(!isAllowedExtentsion(fileName, pattern)){
//            throw new RuntimeException();
//        }
//    }

}
