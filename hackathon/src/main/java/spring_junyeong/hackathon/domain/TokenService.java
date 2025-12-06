package spring_junyeong.hackathon.domain;

public interface TokenService {

  String issueToken(Long userId);

  Boolean validateToken(String token);

  Long getUserIdFromToken(String token);
}
