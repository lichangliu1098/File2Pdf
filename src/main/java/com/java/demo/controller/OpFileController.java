package com.java.demo.controller;


import com.java.demo.service.FileService;
import com.java.demo.util.TransferPdfUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/file")
public class OpFileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/word2pdf")
    public void word2pdf(HttpServletResponse response) {
        //fileService.word2pdf(response);
    }

    @PostMapping("upload2pdf")
    public void upload2pdf(MultipartFile file,String fontName) {

        try {

            System.out.println("fontName======="+fontName);
            String fileName = file.getOriginalFilename();
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            String filePrefix = fileName.substring(0,fileName.lastIndexOf("."));

            byte[] bytes = null;
            if(fileSuffix.equals(".doc") || fileSuffix.equals(".docx")){
                TransferPdfUtil.doc2pdf(file.getInputStream());
            }else if(fileSuffix.equals(".ppt") || fileSuffix.equals(".pptx")){
                TransferPdfUtil.ppt2pdf(file.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("getRootDir")
    public void getRootDit(String font) {
        TransferPdfUtil.getRootDir(font);
    }

}
