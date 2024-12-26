package com.server.storefront.filters;

import com.server.storefront.utils.Path;
import com.server.storefront.utils.PathMatcherUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public class HttpRequestFilter implements HandlerInterceptor {

    private static final String WHOAMI = "whoami";
    private static final String CREATOR = "creator";
    private static final String USER = "user";
    private static final String EXCLUDE_AUTH = "ignoreAuth";
    private static final String BEARER = "Bearer ";

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object object) {
        String path = request.getRequestURI();
        Set<String> sharedPaths = Path.loadSharedPaths();

        if (PathMatcherUtil.matchesPath(path, sharedPaths)) {
            log.info("Request has fallen into Http Request Filter.");
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(BEARER)) {
                request.setAttribute(WHOAMI, USER);
                request.setAttribute(EXCLUDE_AUTH, true);
                log.info("User request is successfully scrubbed and passed down");
            }
            request.setAttribute(EXCLUDE_AUTH, false);
            request.setAttribute(WHOAMI, CREATOR);
        }

        return true;
    }
}
