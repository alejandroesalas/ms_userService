package com.example.userService.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom security filter that intercepts each HTTP request once to validate the JWT token.
 * <p>
 * This filter:
 * <ul>
 *     <li>Extracts the token from the Authorization header.</li>
 *     <li>Validates the token using {@link JwtUtil}.</li>
 *     <li>If valid, sets the authenticated user in the {@link SecurityContextHolder}.</li>
 * </ul>
 * <p>
 * The filter is executed once per request by extending {@link OncePerRequestFilter}.
 */
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    /** Utility for validating and extracting data from JWT tokens. */
    private final JwtUtil jwtUtil;

    /**
     * Filters incoming HTTP requests and performs JWT validation.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isValidToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
