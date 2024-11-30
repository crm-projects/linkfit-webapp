package com.server.storefront.filters;

import com.server.storefront.utils.Path;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.function.Predicate;

@Slf4j
@Component
public class JWTAuthFilter implements HandlerInterceptor {

    private static final String BEARER = "Bearer ";
    private static final String SECRET_KEY = "your_secret_key";

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object object) {

        String path = request.getRequestURI().substring(4);
        Set<String> whitelistedPaths = Path.loadWhiteListedPaths();
        Set<String> swaggerPaths = Path.loadSwaggerPaths();
        Predicate<String> isPathWhitelisted = whitelistedPaths::contains;

        boolean isSwagger = swaggerPaths.stream().anyMatch(path::startsWith);

        if(isSwagger) {
            log.info("Skipping JWT Validation since condition met for path: {}", path);
            return true;
        }

        if (isPathWhitelisted.test(path)) {
            log.info("Skipping JWT Validation since condition met for path: {}", path);
            return true;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            // If JWT is missing, block the request
            log.info("Blocking JWT Validation since condition didn't meet for path: {}", path);

            /*
             * TODO : Should add HttpStatus.UNAUTHORIZED as status code.
             */
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        log.info("Processing JWT Validation since condition didn't meet for path: {}", path);
        // Validate the JWT token
        String token = authHeader.substring(7);
        try {

            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;
        } catch (Exception e) {
            // Token is invalid, block the request
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object object, ModelAndView model) {
        /* TODO document why this method is empty */
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object object, Exception exception) {
        /* TODO document why this method is empty */
    }
}
