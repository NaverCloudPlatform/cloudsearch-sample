package org.ncpclous.sample.cloudsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@RestController
public class RouteConfig {
    private static final Logger log = LoggerFactory.getLogger(RouteConfig.class);

    private final Environment env;

    public RouteConfig(Environment env) {
        this.env = env;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @Bean
    public RouteLocator frontRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("dynamicRoute", r -> r
                        .path("/api/**")
                        .filters(f -> f.filter((exchange, chain) -> {

                            ServerHttpRequest req = exchange.getRequest();
                            String method = req.getMethodValue();
                            String path = req.getURI().getRawPath();
                            String newPath = path.replaceAll("/api", "/CloudSearch/real/v1");

                            ServerHttpRequest request = req.mutate().path(newPath)
                                    .headers(it -> it.addAll(getHeadersMap(method, newPath)))
                                    .build();

                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());

                            return chain.filter(exchange.mutate().request(request).build());
                        }))
                        .uri("https://cloudsearch.apigw.ntruss.com/CloudSearch/real/v1")
                )
                .build();
    }

    public LinkedMultiValueMap<String, String> getHeadersMap(String method, String url) {
        String accessKey = env.getProperty("ncp.accessKey");
        String secretKey = env.getProperty("ncp.secretKey");
        String timestamp = String.valueOf(System.currentTimeMillis());

        try {
            log.info(method + " : " + url);

            String message = String.format("%s %s\n%s\n%s", method, url, timestamp, accessKey);
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put("x-ncp-apigw-timestamp", List.of(timestamp));
            headers.put("x-ncp-iam-access-key", List.of(accessKey));
            headers.put("x-ncp-apigw-signature-v2", List.of(encodeBase64String));

            return headers;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }
}
