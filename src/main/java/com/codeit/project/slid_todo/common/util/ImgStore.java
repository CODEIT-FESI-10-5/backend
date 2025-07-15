package com.codeit.project.slid_todo.common.util;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.exception.errorCode.CommonErrorCode;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Component
public class ImgStore {

    @Value("${img.dir}")
    private String ImgDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

    public UploadImg storeImg(MultipartFile image) throws IOException {
        if (isEmpty(image)) return null;

        validateImage(image);

        String originalFileName = image.getOriginalFilename();
        String storeFileName = generateStoreFileName(originalFileName);

        image.transferTo(new File(getFullPath(storeFileName)));

        return new UploadImg(originalFileName, getFullPath(storeFileName));
    }

    private boolean isEmpty(MultipartFile file) {
        return file == null;
    }

    private void validateImage(MultipartFile image) {
        String originalFileName = image.getOriginalFilename();
        String ext = extractExt(originalFileName);

        if (!ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new BaseException(CommonErrorCode.INVALID_IMAGE_EXTENSION);
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new BaseException(CommonErrorCode.IMAGE_TOO_LARGE);
        }
    }

    private String generateStoreFileName(String originalFilenName) {
        String ext = extractExt(originalFilenName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilenName) {
        int pos = originalFilenName.lastIndexOf(".");
        if (pos == -1) {
            throw new BaseException(CommonErrorCode.IMAGE_EXTENSION_NOT_FOUND);
        }
        return originalFilenName.substring(pos + 1);
    }


    private String getFullPath(String storeFileName) {
        return Paths.get(ImgDir, storeFileName).toString();
    }

    public void deleteImage(String storeFileDir) {
        File file = new File(storeFileDir);
        if (file.exists()) {
            file.delete();
        }
    }
}
