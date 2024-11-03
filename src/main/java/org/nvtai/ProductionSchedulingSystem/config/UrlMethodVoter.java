package org.nvtai.ProductionSchedulingSystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

public class UrlMethodVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final Logger logger = LoggerFactory.getLogger(UrlMethodVoter.class);

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        // Nếu chưa đăng nhập, bỏ qua để hệ thống tự chuyển hướng đến trang đăng nhập
        if (authentication == null || !authentication.isAuthenticated()) {
            return ACCESS_ABSTAIN;
        }

        String method = fi.getHttpRequest().getMethod();
        String url = fi.getHttpRequest().getRequestURI();

        logger.info("Checking access for URL: {} with method: {}", url, method);

        // Kiểm tra quyền theo URL và phương thức
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String requiredPermission = url + ":" + method;
            if (authority.getAuthority().equalsIgnoreCase(requiredPermission)) {
                logger.info("Access granted for authority: {}", requiredPermission);
                return ACCESS_GRANTED;
            }
        }

        logger.warn("Access denied for URL: {} with method: {}", url, method);
        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}