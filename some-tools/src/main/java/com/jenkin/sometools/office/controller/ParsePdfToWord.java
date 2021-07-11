package com.jenkin.sometools.office.controller;

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ParsePdfToWord   {
    /**
     * pdf 2 word
     * @param stream  文件流
     * @param outputStream  文件输出流
     */
    public void parseFileToWord(InputStream stream, OutputStream outputStream) throws Exception {
        try {
            getLicense();
            //sourcePath是将要被转化的word文档
            Document doc = new Document(stream);
            //设置一个字体目录
//            FontSettings.getDefaultInstance().setFontsFolder("/usr/share/fonts", false);

            doc.save(outputStream, SaveFormat.DocX);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
//            if(outputStream!=null) {
//                outputStream.flush();
//            }
//            stream.close();
//            outputStream.close();
        }
    }
    /**
     * 获取授权信息
     *
     * @return
     */
    public boolean getLicense() {

        boolean result = false;
        try {

            String license = "<License>\n" +
                    "  <Data>\n" +
                    "    <Products>\n" +
                    "      <Product>Aspose.Total for Java</Product>\n" +
                    "    </Products>\n" +
                    "    <EditionType>Enterprise</EditionType>\n" +
                    "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                    "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                    "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                    "  </Data>\n" +
                    "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                    "</License>";

            InputStream is = new ByteArrayInputStream(license.getBytes("UTF-8"));
            com.aspose.pdf.License aposeLic = new com.aspose.pdf.License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
