package com.szcgc.cougua.utils;

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AsposePdfUtil {


    public static void main(String[] args) {
//        pdf2doc("C:\\Users\\liuya\\Desktop\\pdf\\中信重工铸锻公司数字化工厂技术要求.pdf");
        //  removeWatermark(new File("C:\\Users\\liuya\\Desktop\\pdf\\中信重工铸锻公司数字化工厂技术要求.docx"));
        pdf2doc("C:\\Users\\18871022312\\Desktop\\111\\余文辉简历.pdf","C:\\Users\\18871022312\\Desktop\\111\\1ywh.docx");

//        pdf2doc("C:\\Users\\18871022312\\Desktop\\222\\余文辉简历_加水印.pdf","C:\\Users\\18871022312\\Desktop\\222\\66.docx");
//        removeWatermark(new File("C:\\Users\\18871022312\\Desktop\\222\\88888.docx"));
    }

    //pdf转doc(目前最大支持21页)
    public static void pdf2doc(String pdfPath,String wordPath) {
        long old = System.currentTimeMillis();
        try {
            //新建一个pdf文档
//            String wordPath=pdfPath.substring(0,pdfPath.lastIndexOf("."))+".docx";
            File file = new File(wordPath);
            FileOutputStream os = new FileOutputStream(file);
            //Address是将要被转化的word文档
            Document doc = new Document(pdfPath);
            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.DocX);
            os.close();
            System.out.println("Pdf 转 Word......");
            //去除Aspose.PDF创建 水印
            removeWatermark(new File(wordPath));
            //转化用时
            long now = System.currentTimeMillis();
            System.out.println("Pdf 转 Word 共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            System.out.println("Pdf 转 Word 失败...");
            e.printStackTrace();
        }
    }

    //移除文字水印
    public static boolean removeWatermark(File file) {
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
            // 段落
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                if ("Evaluation Only. Created with Aspose.PDF. Copyright 2002-2021 Aspose Pty Ltd.".equals(text)) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (XWPFRun xwpfRun : runs) xwpfRun.setText("", 0);
                }
//                if ("编辑试用".equals(text)) {
//                    List<XWPFRun> runs = paragraph.getRuns();
//                    for (XWPFRun xwpfRun : runs) xwpfRun.setText("", 0);
//                }
            }
            FileOutputStream outStream = new FileOutputStream(file);
            doc.write(outStream);
            outStream.close();
            System.out.println("doc去除水印......");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }




}
