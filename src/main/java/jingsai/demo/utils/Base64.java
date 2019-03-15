package jingsai.demo.utils;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64 {
    public static String toBase64W(String image){
        if (image!=null && !image.equals("")) {
            InputStream inputStream = null;
            byte[] data = null;
            String imgFile = ConstantsW.getCurrenPath() + image;
            try {
                inputStream = new FileInputStream(imgFile);
                data = new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            String Data = "data:image/png;base64," + encoder.encode(data);
            return Data;
        }else {
            return null;
        }
    }

    public static String toBase64T(String image){
        if (image!=null && !image.equals("")) {
            InputStream inputStream = null;
            byte[] data = null;
            String imgFile = ConstantsT.getCurrenPath()+image;
            try {
                inputStream = new FileInputStream(imgFile);
                data = new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            String Data = "data:image/png;base64," + encoder.encode(data);
            return Data;
        }else {
            return null;
        }
    }
}
