package com.ust.assignment.filter;

import com.ust.assignment.service.JwtUserDetailsService;
import com.ust.assignment.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    //This is the jwt filter added in JwtConfig, through which all requests pass
    //it extracts the jwt token and verifies it and then allows access to the route


    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if(bearerToken!=null&&bearerToken.startsWith("Bearer")){
            token = bearerToken.substring(7);

        try{
            username = jwtUtil.extractUsername(token);
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        catch (Exception e){
            System.out.println("Invalid bearer token");
            System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("invalid token");
        }
        filterChain.doFilter(request,response);
    }
}
