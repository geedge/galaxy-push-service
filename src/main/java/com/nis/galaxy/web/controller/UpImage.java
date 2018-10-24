package com.nis.galaxy.web.controller;


import com.nis.galaxy.util.DateUtils;
import com.nis.galaxy.util.FileUtil;
import com.nis.galaxy.web.service.UpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UpImage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UpService upService;
    @Value("${Image.Path}")
    private String IMAGEPATH;

    @RequestMapping("/upImage")
    public String upImage() {
        return "index";
    }

    /**
     * 处理文件上传
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/upImageData", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImg(@RequestParam("myfile") MultipartFile file, HttpServletRequest request) {
        long size = file.getSize();
        if (size > 2 * 1024 * 1024) {
            return "请选择图片文件小于2MB";
        }
        if (IMAGEPATH == null || IMAGEPATH.equals("")) {
            try {
                IMAGEPATH = new File("").getCanonicalPath() + "/image/";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> params = new HashMap<>();
        String imageName = file.getOriginalFilename();
        try {
            FileUtil.uploadFile(file.getBytes(), IMAGEPATH, imageName);
        } catch (Exception e) {
            logger.error("上传图片失败\t异常\t", e);
            return "上传图片失败";
        }
        String path = "/image/" + imageName;
        params.put("module", request.getParameter("module").trim());
        params.put("type", request.getParameter("type").trim());
        params.put("url", path);
        params.put("datetime", DateUtils.getDateTime());
        try {
            upService.upImage(params);
        } catch (Exception e) {
            logger.error("储存图片url失败\t异常\t", e);
            return "储存图片url失败";
        }
        return "上传并储存图片===》成功";
    }
}
