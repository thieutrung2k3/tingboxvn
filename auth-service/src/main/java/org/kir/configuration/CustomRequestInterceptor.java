package org.kir.configuration;

import com.kir.commonservice.constant.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String authHeader = requestAttributes.getRequest().getHeader("Authorization");

        if(StringUtils.hasText(authHeader)) {
            requestTemplate.header("Authorization", authHeader);
        }

        requestTemplate.header(SecurityConstants.INTERNAL_HEADER, SecurityConstants.INTERNAL_KEY);
    }
}
