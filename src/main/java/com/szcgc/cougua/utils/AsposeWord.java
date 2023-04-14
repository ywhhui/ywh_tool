package com.szcgc.cougua.utils;


import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class AsposeWord {
    private Map<String,Object> wordMap;

    /**
     * doc to pdf
     *
     * @param docPath
     *            doc源文件
     * @param pdfPath
     *            pdf目标文件
     */
    public static void doc2PDF(String docPath, String pdfPath) {
        FileOutputStream os=null;
        try {
                File file = new File(pdfPath);
                if(!file.exists()){
                    file.createNewFile();
                }
                os = new FileOutputStream(file);
                Document doc = new Document(docPath);
                doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
                System.out.println("doc转pdf成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("doc转pdf出现错误！");
        }finally {
            try {
                if(os!=null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     *   没格式的转
//     * @param destFile
//     * @param docPath
//     */
//    public static void pdf2Word(String destFile,String docPath){
//        PDDocument doc = null;
//        OutputStream fos = null;
//        Writer writer = null;
//        PDFTextStripper stripper = null;
//        try {
////            String destFile = "C:\\Users\\zhubaba\\Desktop\\转化测试.pdf";
//            doc = PDDocument.load(new File(destFile));
//            fos = new FileOutputStream(docPath);
//            writer = new OutputStreamWriter(fos, "UTF-8");
//            stripper = new PDFTextStripper();
//            int pageNumber = doc.getNumberOfPages();
//            stripper.setSortByPosition(true);
//            stripper.setStartPage(1);
//            stripper.setEndPage(pageNumber);
//            stripper.writeText(doc, writer);
//            writer.close();
//            doc.close();
//            System.out.println("pdf转换word完成");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) {

        doc2PDF("D:\\ziyan\\code\\ywh_tool\\10.docx","D:\\ziyan\\code\\ywh_tool\\10yyy.pdf");
//        doc2PDF("C:\\Users\\18871022312\\Desktop\\111\\余文辉简历.pdf","C:\\Users\\18871022312\\Desktop\\111\\6666666666.docx");

//        pdf2Word("C:\\Users\\18871022312\\Desktop\\大数据\\企业洞察接口文档v20210816.pdf","C:\\Users\\18871022312\\Desktop\\大数据\\企业洞察接口文档v20210816.doc");
//        pdf2doc("C:\\Users\\18871022312\\Desktop\\111\\余文辉简历.pdf","C:\\Users\\18871022312\\Desktop\\111\\余文辉简历09081.docx");
//        pdf2doc("C:\\Users\\18871022312\\Desktop\\111\\余文辉简历.pdf");
    }

//    public static void main(String[] args) {
//
//        try {
//            String pdfFileName = "H:\\xuweichao.pdf";
//            PDDocument pdf = PDDocument.load(new File(pdfFileName));
//            int pageNumber = pdf.getNumberOfPages();
//
//            String docFileName = pdfFileName.substring(0, pdfFileName.lastIndexOf(".")) + ".doc";
//
//            File file = new File(docFileName);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            CustomXWPFDocument document = new CustomXWPFDocument();
//            FileOutputStream fos = new FileOutputStream(docFileName);
//
//            //提取每一页的图片和文字，添加到 word 中
//            for (int i = 0; i < pageNumber; i++) {
//
//                PDPage page = pdf.getPage(i);
//                PDResources resources = page.getResources();
//
//                Iterable<COSName> names = resources.getXObjectNames();
//                Iterator<COSName> iterator = names.iterator();
//                while (iterator.hasNext()) {
//                    COSName cosName = iterator.next();
//
//                    if (resources.isImageXObject(cosName)) {
//                        PDImageXObject imageXObject = (PDImageXObject) resources.getXObject(cosName);
//                        File outImgFile = new File("H:\\img\\" + System.currentTimeMillis() + ".jpg");
////                        Thumbnails.of(imageXObject.getImage()).scale(0.9).rotate(0).toFile(outImgFile);
//
//
//                        BufferedImage bufferedImage = ImageIO.read(outImgFile);
//                        int width = bufferedImage.getWidth();
//                        int height = bufferedImage.getHeight();
//                        if (width > 600) {
//                            double ratio = Math.round((double) width / 550.0);
//                            System.out.println("缩放比ratio："+ratio);
//                            width = (int) (width / ratio);
//                            height = (int) (height / ratio);
//
//                        }
//
//                        System.out.println("width: " + width + ",  height: " + height);
//                        FileInputStream in = new FileInputStream(outImgFile);
//                        byte[] ba = new byte[in.available()];
//                        in.read(ba);
//                        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(ba);
//
//                        XWPFParagraph picture = document.createParagraph();
//                        //添加图片
//                        try {
//                            document.addPictureData(byteInputStream, CustomXWPFDocument.PICTURE_TYPE_JPEG);
//                        } catch (InvalidFormatException e) {
//                            e.printStackTrace();
//                        }
//                        //图片大小、位置
//                        document.createPicture(document.getAllPictures().size() - 1, width, height, picture);
//
//                    }
//                }
//
//
//                PDFTextStripper stripper = new PDFTextStripper();
//                stripper.setSortByPosition(true);
//                stripper.setStartPage(i);
//                stripper.setEndPage(i);
//                //当前页中的文字
//                String text = stripper.getText(pdf);
//
//
//                XWPFParagraph textParagraph = document.createParagraph();
//                XWPFRun textRun = textParagraph.createRun();
//                textRun.setText(text);
//                textRun.setFontFamily("仿宋");
//                textRun.setFontSize(11);
//                //换行
//                textParagraph.setWordWrap(true);
//            }
//            document.write(fos);
//            fos.close();
//            pdf.close();
//            System.out.println("pdf转换解析结束！！----");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }









}

