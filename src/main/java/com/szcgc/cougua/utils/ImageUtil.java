package com.szcgc.cougua.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil{
    public static void main(String[] args){
        hebingTP2();

    }

    private static void hebingTP() {
        // 读取背景图片和前景图片
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("D:\\tools\\douyinVideo\\ywh\\2.jpg"));
            BufferedImage foregroundImage = ImageIO.read(new File("D:\\tools\\douyinVideo\\ywh\\1.jpg"));

            // 创建一个透明的缓冲区图片，大小与背景图片相同
            BufferedImage compositeImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            // 获取绘制上下文对象
            Graphics2D graphics = compositeImage.createGraphics();

            // 绘制背景图片
            graphics.drawImage(backgroundImage, 0, 0, null);

            // 设置前景图片的合成模式为源遮罩
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));

            // 绘制前景图片
            graphics.drawImage(foregroundImage, 0, 0, null);

            // 保存结果到文件
            ImageIO.write(compositeImage, "PNG", new File("D:\\tools\\douyinVideo\\ywh\\11.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void hebingTP2() {
        try {
            // Load the source and mask images
            BufferedImage sourceImage = ImageIO.read(new File("D:\\tools\\douyinVideo\\ywh\\2.jpg"));
            BufferedImage maskImage = ImageIO.read(new File("D:\\tools\\douyinVideo\\ywh\\1.jpg"));
            // Create a destination image with the same dimensions as the source image
            BufferedImage destImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            // Iterate over each pixel in the source and mask images
            for (int y = 0; y < sourceImage.getHeight(); y++) {
                for (int x = 0; x < sourceImage.getWidth(); x++) {
                    // Get the color of the current pixel in the source image
                    Color sourceColor = new Color(sourceImage.getRGB(x, y), true);

                    // Get the color of the current pixel in the mask image
                    Color maskColor = new Color(maskImage.getRGB(x, y), true);

                    // If the alpha value of the mask pixel is 0, set the alpha value of the source pixel to 0 in the destination image
                    if (maskColor.getAlpha() == 0) {
                        sourceColor = new Color(0, 0, 0, 0);
                    }

                    // Set the color of the current pixel in the destination image
                    destImage.setRGB(x, y, sourceColor.getRGB());
                }
            }
            // Write the destination image to disk
            ImageIO.write(destImage, "png", new File("D:\\tools\\douyinVideo\\ywh\\output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
