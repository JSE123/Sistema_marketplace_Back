package com.marketplace.MarketBack.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        final Map<String, String> Config = new HashMap<>();
        Config.put("cloud_name", "dayppnink");
        Config.put("api_key", "658811558468439");
        Config.put("api_secret", "uWYaTUS-2wbbk1qNj4Vr20np1F4");
        return new Cloudinary(Config);
    }

//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", cloudName,
//                "api_key", apiKey,
//                "api_secret", apiSecret,
//                "secure", true
//        ));
//    }
}
