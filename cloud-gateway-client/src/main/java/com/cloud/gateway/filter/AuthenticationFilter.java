package com.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.enums.ResultDataEnum;
import com.cloud.common.redis.RedisCache;
import com.fasterxml.jackson.core.filter.TokenFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 鉴权相关
 * 拦截所有请求，校验token
 */
@Component
public class AuthenticationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    public static final String ADDRESS = "127.0.0.1:51201";

    @Autowired
    private RedisCache redisCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        request.mutate().header("param", "11").build();

//        logger.info("请求信息为{}", JSONObject.toJSONString(exchange.getRequest().getHeaders()));
//
//        String token = exchange.getRequest().getHeaders().getFirst("token");
//        JSONObject jsonObject = new JSONObject();
//
//        ServerHttpResponse response = exchange.getResponse();
//        if(StringUtils.isBlank(token)) {
//            logger.info("token 为空!");
//            jsonObject.put("code", ResultDataEnum.AUTH_INVALID.code());
//            jsonObject.put("message", ResultDataEnum.AUTH_INVALID.desc());
//            return commonAuth(response, jsonObject, HttpStatus.UNAUTHORIZED);
//        } else {
//            String key = String.format(RedisConstants.USER_TOKEN, token);
//            String redisToken = redisCache.getString(key);
//            if(!token.equals(redisToken)) {//token不相同
//                logger.info("token 不合法!");
//                jsonObject.put("code", ResultDataEnum.AUTH_INVALID.code());
//                jsonObject.put("message", ResultDataEnum.AUTH_INVALID.desc());
//                return commonAuth(response, jsonObject, HttpStatus.UNAUTHORIZED);
//            }
//
//            //校验网站来源 避免CRSF等
//            String origin = exchange.getRequest().getHeaders().getOrigin();
//            if(!ADDRESS.equals(origin)){
//                jsonObject.put("code", "5555");
//                jsonObject.put("message", "来源校验失败!");
//                return commonAuth(response, jsonObject, HttpStatus.UNAUTHORIZED);
//            }
//
//        }
        return chain.filter(exchange);
    }

    private Mono<Void> commonAuth(ServerHttpResponse response, JSONObject jsonObject, HttpStatus httpCode){
        byte[] bits = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(httpCode);
        response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

}
