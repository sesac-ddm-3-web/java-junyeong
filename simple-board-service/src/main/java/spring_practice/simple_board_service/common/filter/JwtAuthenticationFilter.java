package spring_practice.simple_board_service.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import spring_practice.simple_board_service.application.auth.TokenService;
import spring_practice.simple_board_service.domain.auth.AuthenticationContext;
import spring_practice.simple_board_service.presentation.auth.dto.UserPrincipal;

/**
 * JWT 토큰을 추출하고 검증하여, 유효한 경우 사용자 정보를 AuthenticationContext에 설정하는 필터.
 */
public class JwtAuthenticationFilter implements Filter {

  private final TokenService tokenService;

  public JwtAuthenticationFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    if (isExcludedPath(httpRequest)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      String jwt = resolveToken(httpRequest);

      if (jwt != null) {
        if (tokenService.validateToken(jwt)) {
          String userId = tokenService.getClaims(jwt).getSubject();

          UserPrincipal principal = new UserPrincipal(Long.parseLong(userId));
          AuthenticationContext.setPrincipal(principal);
        }
      }

      if (jwt == null) {
        unauthorized(response, "로그인이 필요한 서비스입니다.");
        return;
      }

      if (!tokenService.validateToken(jwt)) {
        unauthorized(response, "유효하지 않은 인증 정보입니다. 다시 로그인해 주세요.");
        return;
      }

      chain.doFilter(request, response);

    } finally {
      AuthenticationContext.clear();
    }
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private boolean isExcludedPath(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();

    if (path.startsWith("/auth/sign-in")) {
      return true;
    }
    if (path.startsWith("/auth/sign-up")) {
      return true;
    }

    if ("GET".equalsIgnoreCase(method)) {
      return true;
    }

    return false;
  }

  private void unauthorized(ServletResponse response, String message) throws IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpResponse.setContentType("application/json; charset=UTF-8");
    httpResponse.getWriter().write("{\"error\":\"" + message + "\"}");
  }
}