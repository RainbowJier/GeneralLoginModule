package com.example.gateway.filter;


import cn.dev33.satoken.stp.StpUtil;
import com.example.gateway.model.SecurityProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * AuthFilter
 */

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${Authorization}")
    private String Authorization;


    @Resource
    private SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // request path.
        String url = request.getURI().getPath();

        // pass out the specific urls.
        if (securityProperties.isIgnored(url, securityProperties.getIgnoredUrls())) {
            return chain.filter(exchange);
        }

        // Get token from request header.
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(token == null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Common token to pass.
        if(token.equals(Authorization)){
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }

        // Check the token is valid or not.
        String loginId = (String) StpUtil.getLoginIdByToken(token);
        if(loginId == null){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        mutate.header("loginId", loginId);

        // todo:token 续约
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }




    @Override
    public int getOrder() {
        return 0;
    }
}
