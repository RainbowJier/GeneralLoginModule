package com.example.gateway.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix ="security")
public class SecurityProperties {

    private List<String> ignoredUrls;

    private List<String> interceptedUrls;

    AntPathMatcher antPathMatcher = new AntPathMatcher();


    public boolean isIgnored(String requestUrl, List<String> list) {
        for (String url : list) {
            if (antPathMatcher.match(url, requestUrl)) return true;
        }
        return false;
    }
}
