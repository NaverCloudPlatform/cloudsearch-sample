package org.ncpclous.sample.cloudsearch;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private Environment env;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        String accessKey = env.getProperty("ncp.accessKey");
        String secretKey = env.getProperty("ncp.secretKey");
        String timestamp = String.valueOf(System.currentTimeMillis());

        ctx.addZuulRequestHeader("x-ncp-apigw-timestamp", timestamp);
        ctx.addZuulRequestHeader("x-ncp-iam-access-key", accessKey);

        try {
            HttpServletRequest request = ctx.getRequest();
            String method = request.getMethod();
            String url = request.getRequestURI().replace("/api", "/CloudSearch/real/v1");
            String message = String.format("%s %s\n%s\n%s", method, url, timestamp, accessKey);

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);
            ctx.addZuulRequestHeader("x-ncp-apigw-signature-v2", encodeBase64String);

            log.info(method + " : " + request.getRequestURL());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }
}
