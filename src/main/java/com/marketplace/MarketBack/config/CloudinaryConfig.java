package com.marketplace.MarketBack.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {


    private final CloudinaryProperties properties;

    public CloudinaryConfig(CloudinaryProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> Config = new HashMap<>();
        Config.put("cloud_name", this.properties.getCloudName());
        Config.put("api_key", this.properties.getApiKey());
        Config.put("api_secret", this.properties.getApiSecret());
        return new Cloudinary(Config);
    }
//Config.put("cloud_name", "dayppnink");
//        Config.put("api_key", "658811558468439");
//        Config.put("api_secret", "uWYaTUS-2wbbk1
}