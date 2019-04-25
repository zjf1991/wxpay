package com.yzj.wxpay.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRcode {


    // 二维码颜色
    private static final int BLACK = 0xFF000000;
    // 二维码颜色
    private static final int WHITE = 0xFFFFFFFF;

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /**
     * 生成不带图片的二维码
     * @param url
     * @return
     */
    public static void getQRcode(String url, HttpServletResponse response){
        try {
            int width=300;
            int height=300;

            //创建MAP集合
            Map<EncodeHintType,Object> hintTypeObjectMap=new HashMap<>();
            hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            hintTypeObjectMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hintTypeObjectMap.put(EncodeHintType.MARGIN, 1);
            //创建一个矩阵对象
            BitMatrix bitMatrix=new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE,width,height,hintTypeObjectMap);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            //输出到流
            outPic(image,response);

        }catch (Exception e){

        }
    }

    /**
     * 生成带图片的二维码图片
     * @param url
     * @return
     */
    public static void getQRcodeWithPic(String url, String imgPath, boolean needCompress, HttpServletResponse response){
        try {
            int width=300;
            int height=300;

            //创建MAP集合
            Map<EncodeHintType,Object> hintTypeObjectMap=new HashMap<>();
            hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            hintTypeObjectMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hintTypeObjectMap.put(EncodeHintType.MARGIN, 1);

            //创建一个矩阵对象
            BitMatrix bitMatrix=new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE,width,height,hintTypeObjectMap);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            //在生成的二维码中插入图片
            insertImage(image,imgPath,needCompress);
            //输出到流
            outPic(image,response);
        }catch (Exception e){

        }
    }

    /**
     * 在生成的二维码中插入图片
     *
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 输出到流
     * @param
     * @return
     */
    private static void outPic(BufferedImage image, HttpServletResponse response){
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            IOUtils.closeQuietly(out);
        }catch (Exception e){

        }
    }

    /**
     * 输出到文件
     * @param
     * @return
     */
    private static void outToFile(BufferedImage image, String outPath,String fileName){
        try {
            File file=new File(outPath);
            if(!file.exists()){
                file.mkdirs();
            }
            file=new File(outPath+File.separator+fileName);
            ImageIO.write(image, "jpg", file);
        }catch (Exception e){

        }
    }
}
