package ru.ershov.project.orderservice.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ershov.project.orderservice.security.JWTUtil;
import ru.ershov.project.orderservice.services.UsersDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final Set<String> EXCLUDED_PATH_PREFIXES = Set.of(
            "/api/v1/orders/auth/register",
            "/api/v1/orders/auth/login",
            "/api/v1/orders/swagger-ui/",
            "/api/v1/orders/v3/api-docs",
            "/error"
    );
    private static final int AUTH_HEADER = 7;
    private final JWTUtil jwtUtil;
    private final UsersDetailsService usersDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(request.getHeader("Authorization"));
        if (jwt == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        try {
            authenticate(jwt);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(AUTH_HEADER);
        }
        return null;
    }

    private void authenticate(String jwt) {
        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
        UserDetails userDetails = usersDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities()
        );

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATH_PREFIXES.stream().anyMatch(path::startsWith);
    }
}