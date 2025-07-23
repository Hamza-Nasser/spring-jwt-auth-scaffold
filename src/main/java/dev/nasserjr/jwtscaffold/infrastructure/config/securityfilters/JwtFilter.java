package dev.nasserjr.jwtscaffold.infrastructure.config.securityfilters;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.nasserjr.jwtscaffold.application.configservice.IJwtService;
import dev.nasserjr.jwtscaffold.application.configservice.JwtService;
import dev.nasserjr.jwtscaffold.application.configservice.UserDetailsServiceImpl;
import dev.nasserjr.jwtscaffold.domain.model.AuthenticatedUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final IJwtService jwtService;

  private final ApplicationContext context;

  public JwtFilter(JwtService jwtService, ApplicationContext context) {
    this.jwtService = jwtService;
    this.context = context;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      email = jwtService.extractEmail(token);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      AuthenticatedUserPrincipal userPrincipal = (AuthenticatedUserPrincipal) context
          .getBean(UserDetailsServiceImpl.class).loadUserByUsername(email);
      if (jwtService.isTokenValid(token, userPrincipal)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPrincipal, null,
            userPrincipal.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }

}
