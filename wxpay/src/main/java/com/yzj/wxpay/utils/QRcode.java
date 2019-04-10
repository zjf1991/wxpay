package com.yzj.wxpay.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRcode {


    /**
     * 生成支付二维码图片
     * @param url
     * @return
     */
    public static void getQRcode(String url, HttpServletResponse response){
        try {
            int width=200;
            int height=200;

            //创建MAP集合
            Map<EncodeHintType,Object> hintTypeObjectMap=new HashMap<>();
            hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");

            //创建一个矩阵对象
            BitMatrix bitMatrix=new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE,width,height,hintTypeObjectMap);
            //创建字节输出流
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            //将矩阵对象转换流响应到页面
            MatrixToImageWriter.writeToStream(bitMatrix,"jpg",byteArrayOutputStream);
            //字节数据输入流
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            //创建一个图片缓存对象
            BufferedImage bufferedImage=ImageIO.read(byteArrayInputStream);
            //输出流对象
            OutputStream outputStream=response.getOutputStream();
            ImageIO.write(bufferedImage,"jpg",outputStream);

            bufferedImage.flush();
            outputStream.flush();
            outputStream.close();


        }catch (Exception e){

        }
    }
}
