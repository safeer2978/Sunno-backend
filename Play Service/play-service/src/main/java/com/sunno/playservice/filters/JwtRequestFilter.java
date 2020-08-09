package com.sunno.playservice.filters;


import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;
import com.sunno.playservice.model.AuthenticationToken;
import com.sunno.playservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String access = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            JSONObject jsonObject = new JSONObject(jwtUtil.extractJsonSubject(jwt));
            JSONArray jsonArray = jsonObject.getJSONArray("authorities");
            access = (String) jsonArray.get(0);
        }


        if (access != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                String finalAccess = access;

                GrantedAuthority authority = new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return finalAccess;
                    }
                };
                authorities.add(authority);
                AuthenticationToken authenticationToken = new AuthenticationToken(authorities);

                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                authenticationToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
