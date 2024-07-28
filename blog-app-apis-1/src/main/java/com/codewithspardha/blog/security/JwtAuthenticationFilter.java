package com.codewithspardha.blog.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 1. get token
		
		String requestToken = request.getHeader("Authorization");
		logger.info("Header : {}", requestToken);
		//Bearer 68798798986y875
		//System.out.println(requestToken);
		
		String username = null;
		
		String token = null;
		
		if(requestToken !=  null && requestToken.startsWith("Bearer")) {
			 token = requestToken.split(" ")[1].trim();
			 try {
				 username = this.jwtTokenHelper.getUsernameFromToken(token);
			 }
			 catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			}
			 catch(ExpiredJwtException e) {
				 logger.info("Given jwt token is expired!!");
				 e.printStackTrace();
			 }
			 catch (MalformedJwtException e) {
				 logger.info("Some changed has been done in token !! Invalid token");
				 e.printStackTrace();
			}
			 catch(Exception e) {
				 e.printStackTrace();
			 }
			 
		}
		else {
			System.out.println("Jwt token does not begin with Bearer");
		}
		
		//once we get the token, now validate
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else {
				logger.info("Validation fails!!");
			}
		}
		else {
			logger.info("Username is null or context is not null");
		}
		
		filterChain.doFilter(request, response);
	}

}
