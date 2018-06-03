package com.snapadeal.services;


import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ImageService {

//    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//        "cloud_name", "dlbwgbupa",
//        "api_key", "485516282662627",
//        "api_secret", "OhoWbmYGU-BGwMMI-jHbLFjAV1E@dlbwgbupa");

    public Map<String,String> updateImage(File fileToUpload)
    {
        Map<String,String> uploadResult = null;
        Cloudinary cloudinary = Singleton.getCloudinary();
        try {
             uploadResult = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return uploadResult;

    }

    public String getImageUrl(String publicId, int height,int width)
    {
        String imageUrl = null
        Cloudinary cloudinary = Singleton.getCloudinary();
        imageUrl = cloudinary.url().transformation(new Transformation().width(width).height(height)).generate(publicId);
        return imageUrl;

    }

}
