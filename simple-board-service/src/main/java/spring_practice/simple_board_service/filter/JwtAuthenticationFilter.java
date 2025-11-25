package spring_practice.simple_board_service.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import spring_practice.simple_board_service.application.TokenService;
import spring_practice.simple_board_service.domain.AuthenticationContext;
import spring_practice.simple_board_service.domain.UserPrincipal;

/**
 * JWT 토큰을 추출하고 검증하여, 유효한 경우 사용자 정보를 AuthenticationContext에 설정하는 필터.
 */
public class JwtAuthenticationFilter implements Filter {

  private final TokenService tokenService;

  // TokenService를 주입받아 사용
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
      // 1. 요청 헤더에서 JWT 토큰 추출
      String jwt = resolveToken(httpRequest);

      if (jwt != null) {
        // 2. 토큰 유효성 검증
        if (tokenService.validateToken(jwt)) {
          // 3. 토큰에서 Claims(사용자 ID) 추출
          String userId = tokenService.getClaims(jwt).getSubject();

          // 4. 추출된 정보를 UserPrincipal 객체로 만들고 Context에 저장
          UserPrincipal principal = new UserPrincipal(Long.parseLong(userId));
          AuthenticationContext.setPrincipal(principal);
        }
      }

      if (jwt == null) {
        unauthorized(response, "로그인 필요한 서비스입니다.");
        return;
      }

      if (!tokenService.validateToken(jwt)) {
        unauthorized(response, "유효하지 않은 인증 정보입니다. 다시 로그인해 주세요.");
        return;
      }

      // 5. 다음 필터 또는 서블릿(Controller)으로 요청 전달
      chain.doFilter(request, response);

    } finally {
      // 6. 요청 처리 완료 후, ThreadLocal에 저장된 인증 정보 정리 (필수!)
      AuthenticationContext.clear();
    }
  }

  /**
   * HTTP Authorization 헤더에서 JWT 토큰을 추출합니다. (Bearer 스키마 가정)
   */
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

    // 인증이 필요 없는 URL 목록
    if (path.startsWith("/auth/sign-in")) {
      return true;
    }
    if (path.startsWith("/auth/sign-up")) {
      return true;
    }

    // GET 요청은 모두 허용하고 싶다면
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