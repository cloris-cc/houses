package org.cloris.houses.biz.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    // Nginx 静态文件路径
    @Value("${file.path}")
    private String filePath;

    @Value("${local.image}")
    private String localImage;

    /**
     * 保存用户上传的头像到本地。返回的 ImgPath 最终要存到数据库中。
     *
     * @param files 上传的头像图片文件。
     * @return avatar 的路径。
     */
    public List<String> getImgPaths(List<MultipartFile> files) {
        List<String> paths = new ArrayList<>();
        files.forEach(file -> {
            long timestamp = Instant.now().getEpochSecond();
            File local = new File(localImage + "/" + timestamp + "/" + file.getOriginalFilename());
            System.out.println("路径是否创建：" + local.getParentFile().mkdirs());
            try {
                file.transferTo(local);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = StringUtils.substringAfterLast(filePath + "/" + timestamp + "/" + file.getOriginalFilename(), filePath);
            paths.add(path);
        });
        return paths;
    }

}
