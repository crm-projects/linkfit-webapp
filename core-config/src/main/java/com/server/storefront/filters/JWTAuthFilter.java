package com.server.storefront.filters;

import com.server.storefront.configs.InitLoader;
import com.server.storefront.utils.Path;
import com.server.storefront.utils.PathMatcherUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthFilter implements HandlerInterceptor {

    private static final String BEARER = "Bearer ";
    private static final String SECRET_KEY = "your_secret_key";

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object object) {

        boolean ignoreAuth = (boolean) request.getAttribute("ignoreAuth");

        if (InitLoader.getJwtVerification() || ignoreAuth) {
            return true;
        }

        String path = request.getRequestURI();
        Set<String> whitelistedPaths = Path.loadWhiteListedPaths();
        Set<String> swaggerPaths = Path.loadSwaggerPaths();
        Set<String> sharedPaths = Path.loadSharedPaths();
        Predicate<String> isPathWhitelisted = whitelistedPaths::contains;

        boolean isSwagger = swaggerPaths.stream().anyMatch(path::startsWith);

        if (isSwagger) {
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
                    .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY)))
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
}
