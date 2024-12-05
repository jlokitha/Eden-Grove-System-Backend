package lk.ijse.Green_Shadow_Backend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class ConvertToBase64 {
    public static String toBase64Image(MultipartFile image){
        String base64Img = null;
        try {
            byte [] proPicBytes = image.getBytes();
            base64Img =  Base64.getEncoder().encodeToString(proPicBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return base64Img;
    }
}
