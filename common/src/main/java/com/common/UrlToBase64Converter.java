package com.common;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UrlToBase64Converter {

    /**
     * 将URL指向的文件转换为Base64字符串
     * @param fileUrl 文件的URL地址
     * @return Base64编码的字符串
     * @throws IOException 如果发生网络或IO错误
     */
    public static String convertUrlToBase64(String fileUrl) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            // 创建URL连接
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5秒连接超时
            connection.setReadTimeout(15000);  // 15秒读取超时

            // 检查响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP请求失败，响应码: " + responseCode);
            }

            // 获取文件内容
            inputStream = connection.getInputStream();
            outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 转换为Base64
            byte[] fileBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(fileBytes);

        } finally {
            // 确保资源被正确关闭
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 忽略关闭时的异常
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // 忽略关闭时的异常
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        // 示例用法
        String fileUrl = "http://202.111.131.240:183/file/construction-site/contract/70288157/梁路坡.pdf";
        // String fileUrl = "http://timeloit-zhgd.oss-cn-beijing.aliyuncs.com/tijian20250219/LAEHCHQQLRCBWAYO/511025197301225271_林君.jpg";
        try {
            // String base64String = convertUrlToBase64(fileUrl);
            // System.out.println("Base64编码结果:");
            // System.out.println(base64String);
            //
            // // 如果需要，可以获取文件类型
            // String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            // System.out.println("文件名称: " + fileName);
            //
            // // 如果需要，可以获取文件类型
            // String fileType = fileUrl.substring(fileUrl.lastIndexOf('.') + 1);
            // System.out.println("文件类型: " + fileType);

            // 压缩参数设置
            float imageQuality = 0.2f;    // 图像质量 (0.1-1.0)
            float pageScale = 0.2f;      // 页面缩放比例 (0.1-1.0)
            int dpi = 70;               // 渲染DPI (72-150)
            String base64 = compressPDFToBase64(fileUrl, imageQuality, pageScale, dpi);
            byte[] bytes =Base64.getDecoder().decode(base64);
            saveByteArrayToFile(bytes,"/Users/huagang/Downloads/111.pdf");
            System.out.println("压缩后的Base64编码结果:"+base64);
        } catch (IOException e) {
            System.err.println("转换失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveByteArrayToFile(byte[] data, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    /**
     * 压缩PDF文件
     * @param fileUrl 输入url
     * @param imageQuality 图像质量 (0.1-1.0)
     * @param pageScale 页面缩放比例 (0.1-1.0)
     * @param dpi 渲染DPI (建议72-150)
     * @throws IOException
     */
    public static String compressPDFToBase64(String fileUrl, float imageQuality, float pageScale, int dpi) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        PDDocument originalDoc = null;
        PDDocument compressedDoc = null;
        try {
            if (imageQuality <= 0 || imageQuality > 1) {
                throw new IllegalArgumentException("图像质量必须在0.1到1.0之间");
            }
            if (pageScale <= 0 || pageScale > 1) {
                throw new IllegalArgumentException("缩放比例必须在0.1到1.0之间");
            }

            // 创建URL连接
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5秒连接超时
            connection.setReadTimeout(15000);  // 15秒读取超时
            // 检查响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP请求失败，响应码: " + responseCode);
            }

            inputStream = connection.getInputStream();
            // 加载原始PDF
            originalDoc = PDDocument.load(inputStream);
            compressedDoc = new PDDocument();

            // 创建PDF渲染器
            PDFRenderer renderer = new PDFRenderer(originalDoc);

            System.out.println("开始处理PDF，共" + originalDoc.getNumberOfPages() + "页...");
            // 处理每一页
            for (int i = 0; i < originalDoc.getNumberOfPages(); i++) {
                System.out.println("正在处理第 " + (i + 1) + " 页...");

                // 获取原始页面信息
                PDPage originalPage = originalDoc.getPage(i);
                PDRectangle originalMediaBox = originalPage.getMediaBox();

                // 计算缩放后的页面尺寸
                float scaledWidth = originalMediaBox.getWidth() * pageScale;
                float scaledHeight = originalMediaBox.getHeight() * pageScale;
                PDRectangle scaledMediaBox = new PDRectangle(scaledWidth, scaledHeight);

                // 创建新页面
                PDPage newPage = new PDPage(scaledMediaBox);
                compressedDoc.addPage(newPage);

                // 渲染原始页面为图像（使用指定DPI）
                BufferedImage renderedImage = renderer.renderImage(i, dpi / 72f);

                // 压缩图像质量
                BufferedImage compressedImage = compressImage(renderedImage, imageQuality);

                // 将压缩后的图像添加到新页面
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        compressedDoc, newPage,
                        PDPageContentStream.AppendMode.APPEND, true)) {

                    PDImageXObject imageXObject = LosslessFactory.createFromImage(compressedDoc, compressedImage);
                    contentStream.drawImage(imageXObject, 0, 0, scaledWidth, scaledHeight);
                }
            }

            // 设置文档信息（可选）
            compressedDoc.setDocumentInformation(originalDoc.getDocumentInformation());
            // 移除所有安全限制（如果有）
            compressedDoc.setAllSecurityToBeRemoved(true);

            // 检查压缩结果
            // File outputFile = new File(outputPath);
            // long compressedSize = outputFile.length() / 1024;

            // 使用ByteArrayOutputStream来获取PDF的字节内容
            outputStream = new ByteArrayOutputStream();
            compressedDoc.save(outputStream);
            // 将字节转换为Base64字符串
            // long compressedSize = base64Str.getBytes(StandardCharsets.UTF_8).length;
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } finally {
            // 确保资源被正确关闭
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 忽略关闭时的异常
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // 忽略关闭时的异常
                }
            }
            if (originalDoc != null) {
                originalDoc.close();
            }
            if (compressedDoc != null) {
                compressedDoc.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 压缩图像质量
     * @param originalImage 原始图像
     * @param quality 压缩质量 (0.1-1.0)
     * @return 压缩后的图像
     */
    private static BufferedImage compressImage(BufferedImage originalImage, float quality) {
        // 这里使用简单的缩放方法，实际项目中可以使用更高级的压缩算法
        if (quality >= 0.9f) {
            return originalImage; // 高质量时不压缩
        }

        // 计算目标尺寸
        int targetWidth = (int) (originalImage.getWidth() * quality);
        int targetHeight = (int) (originalImage.getHeight() * quality);

        // 创建缩放后的图像
        BufferedImage compressedImage = new BufferedImage(
                targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        // 绘制缩放后的图像
        compressedImage.createGraphics().drawImage(
                originalImage.getScaledInstance(
                        targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH),
                0, 0, null);

        return compressedImage;
    }

    public static void compressImage(String inputPath, String outputPath, long targetSizeKB) throws IOException {
        File inputFile = new File(inputPath);
        long originalSizeKB = inputFile.length() / 1024;
        System.out.println("原始文件大小: " + originalSizeKB + " KB");

        // 初始缩放比例
        double scale = 1.0;
        int iterations = 0;
        long currentSizeKB = originalSizeKB;

        while (currentSizeKB > targetSizeKB && iterations < 10) {
            iterations++;
            // 调整缩放比例
            scale *= 0.9;

            // 使用Thumbnails进行压缩
            Thumbnails.of(inputPath)
                    .scale(scale)
                    .outputQuality(0.8) // 固定质量参数
                    .toFile(outputPath);

            // 检查新大小
            File outputFile = new File(outputPath);
            currentSizeKB = outputFile.length() / 1024;
            System.out.println("迭代 " + iterations + ": 缩放=" + scale +
                    ", 大小=" + currentSizeKB + " KB");
        }

        if (currentSizeKB <= targetSizeKB) {
            System.out.println("压缩成功! 最终大小: " + currentSizeKB + " KB");
        } else {
            System.out.println("压缩后大小: " + currentSizeKB + " KB (未达到目标)");
        }
    }
}