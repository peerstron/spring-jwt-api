package com.peers.jwtauthentication.config;

import com.peers.jwtauthentication.utils.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// This filter will always be active in order to catch every request and response in and out of the application
// Here I extend the OncePerRequestFilter so I can utilize the already made method provided for filtering.
// In order for it to be always active and ready to use we add the @Component annotation and then it becomes a managed bean
@Component
@RequiredArgsConstructor    // This helps to automatically create a constructor with any private variable declared final in this class
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtilService jwtUtilService;

    @Override
    // With this method I can intercept every request and extract data from it to verify for example who is making the request
    // or even add data like response header to the response.
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // We try to extract Authorization from the request header
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String staffEmail;

        // If nothing is found or its not what we expect then we simply sto the user request
        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            filterChain.doFilter(request, response);
            return;
        }

        // If the Authorization value we expect is there then we extract the token from it
        jwtToken = authHeader.substring(7);

        // Validate jwt token to extract staff email
        staffEmail = jwtUtilService.extractStaffEmail(jwtToken);
    }
}
