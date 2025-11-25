package kr.co.hanbit.product.management.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import kr.co.hanbit.product.management.application.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

  private final UserService userService; // 토큰 검증 로직을 가진 Service 주입

  // 인증 제외 URL 목록 정의 (White List)
  private static final List<String> AUTH_EXEMPT_PATHS = List.of(
      "/",
      "/api/auth/sign-in",
      "/api/auth/sign-up"
  );

  public JwtVerificationFilter(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestPath = request.getRequestURI();
    String requestMethod = request.getMethod();

    // 1. **인증 제외 URL 처리 (permitAll)**
    // 로그인, 회원가입 API나 GET /products는 인증 없이 통과
    if (AUTH_EXEMPT_PATHS.contains(requestPath) ||
        (requestPath.equals("/products") && requestMethod.equals(HttpMethod.GET.name()))) {

      filterChain.doFilter(request, response);
      return;
    }

    // --- 인증이 필요한 요청에 대한 처리 시작 ---

    String token = extractJwtFromRequest(request);

    // 2.1. **토큰이 없는 경우 401 Unauthorized 반환** (수정된 핵심 로직)
    if (token == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Missing Authorization token.");
      return; // 요청 처리 종료
    }

    // 2.2. **토큰 유효성 검증**
    try {
      // 토큰 유효성 검증 및 User ID 추출 (UserService가 담당)
      Long userId = userService.verifyAndGetUserId(token);

      // 3. **인증 정보 저장** (Request Attribute에 User ID 저장)
      request.setAttribute("USER_ID", userId);

    } catch (Exception e) {
      // 토큰이 유효하지 않으면 (만료, 변조 등) 401 에러 응답
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token.");
      return;
    }

    // 4. 다음 필터 또는 Controller로 요청 전달
    filterChain.doFilter(request, response);
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }


}