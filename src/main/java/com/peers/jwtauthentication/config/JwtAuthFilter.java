package com.peers.jwtauthentication.config;

import com.peers.jwtauthentication.utils.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    private UserDetailsService staffDetailsService;

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
        staffEmail = jwtUtilService.extractUsername(jwtToken);

        // Here we check to make sure we got the staff email and also check if the user is already authenticated
        if(staffEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // If we get the staff email but the staff is not authenticated then we get the staff details from db
            UserDetails staffDetails = staffDetailsService.loadUserByUsername(staffEmail);
            if(jwtUtilService.isTokenValid(jwtToken, staffDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        staffDetails,
                        null,
                        staffDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass handler to next response after authenticating
        filterChain.doFilter(request, response);
    }
}
