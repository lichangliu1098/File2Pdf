package com.java.demo.util;

import com.aspose.slides.FontsLoader;
import com.aspose.slides.IFontData;
import com.aspose.slides.PdfCompliance;
import com.aspose.slides.PdfOptions;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.LoadOptions;
import com.aspose.words.SaveFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TransferPdfUtil {

    private static final String DEFAULT = "pdf/default";

    private static final String PDF_SUFFIX = ".pdf";


    private static boolean getWordLicense() {
        boolean result = false;
        try {
            InputStream is = TransferPdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static boolean getPptLicense() {
        boolean result = false;
        try {
            InputStream is = TransferPdfUtil.class.getClassLoader().getResourceAsStream("license.xml");

            com.aspose.slides.License aposeLic = new com.aspose.slides.License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getRootDir(String font){
       String currentPath = TransferPdfUtil.class.getClassLoader().getResource("").getPath();

       if(Objects.nonNull(font) && font.length() > 0){
           currentPath = currentPath + font;
       }
       System.out.println("current=========="+currentPath);

       return currentPath;
    }


    /**
     * word转pdf
     * @param inPath
     * @param outPath
     */
    public static void doc2pdf(String inPath, String outPath) {
        if (!getWordLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            //引用外部的字体，实验证明，源文件使用的字体要和外部引用的要对应，不然转出来的pdf的字体会乱码
            FontSettings fontSettings = new FontSettings();
            fontSettings.setFontsFolder("/font",true);//true为是否递归文件夹
            LoadOptions loadOptions = new LoadOptions();
            loadOptions.setFontSettings(fontSettings);
            Document doc = new Document(inPath,loadOptions); // Address是将要被转化的word文档

            doc.save(outPath, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String defaultFilename = DEFAULT+ System.currentTimeMillis() +PDF_SUFFIX;
        ppt2pdf("ppt/ppt3.pptx",defaultFilename);
    }

    public static void doc2pdf(InputStream inputStream) {

        byte[] bytes = null;
        if (!getWordLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }

        long now = System.currentTimeMillis();
        String defaultFilename = DEFAULT+ System.currentTimeMillis() +PDF_SUFFIX;
        try {
            long old = System.currentTimeMillis();

            //下面两行为引用外部的字体的代码，实验证明，源文件使用的字体和外部引用的字体要对应，不然转出来的pdf的字体会乱码，注意文件夹的位置mac和windows注意区分
            //font为引入的文件夹名称，根据自己所配置的文件夹进行调整
            FontSettings fontSettings = new FontSettings();
            fontSettings.setFontsFolder("/font",true);
            LoadOptions loadOptions = new LoadOptions();
            loadOptions.setFontSettings(fontSettings);

            Document doc = new Document(inputStream,loadOptions); // Address是将要被转化的word文档

            doc.save(defaultFilename, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换

            //转化成byte数组，上传到oss
            /*File file = new File(defaultFilename);
            bytes = readInputStream(new FileInputStream(file));
            if(file.exists()){
                //把文件删除
                file.delete();
            }*/

            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            FontsLoader.clearCache();
        }
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean var4 = false;

        int len;
        while((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static void ppt2pdf(InputStream inputStream) {

        byte[] bytes = null;
        // 验证License
        if (!getPptLicense()) {
            return;
        }

        //下面两行为引用外部的字体的代码，实验证明，源文件使用的字体和外部引用的字体要对应，不然转出来的pdf的字体会乱码，注意文件夹的位置mac和windows注意区分
        //font为引入的文件夹名称，根据自己所配置的文件夹进行调整
        String[] folders = new String[]{"/font"};
        FontsLoader.loadExternalFonts(folders);

        Presentation pres = new Presentation(inputStream);
        FileOutputStream fileOS = null ;

        //默认生成的文件路径名称
        String defaultFilename = DEFAULT + System.currentTimeMillis() + PDF_SUFFIX;
        try {

            File file = new File(defaultFilename);// 输出pdf路径
            long old = System.currentTimeMillis();

            pres.save(defaultFilename, com.aspose.slides.SaveFormat.Pdf);

            //转化成byte数组，上传到oss
           /* bytes = readInputStream(new FileInputStream(file));
            if(file.exists()){
                //把文件删除
                file.delete();
            }*/

            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(pres != null){
                pres.dispose();
            }

            if(fileOS != null) {
                try {
                    fileOS.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            //清理缓存
            FontsLoader.clearCache();
        }

    }

    /**
     * ppt转pdf
     * @param inPath
     * @param outPath
     */
    public static void ppt2pdf(String inPath,String outPath) {

        // 验证License
        if (!getPptLicense()) {
            return;
        }
        //引用外部的字体
        String[] folders = new String[]{"/font"};
        FontsLoader.loadExternalFonts(folders);
        Presentation pres = new Presentation(inPath);//输入pdf路径
        try {

            pres.save(outPath, com.aspose.slides.SaveFormat.Pdf);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(pres != null){
                pres.dispose();
            }
            //清空字体缓存
            FontsLoader.clearCache();
        }
    }


}
