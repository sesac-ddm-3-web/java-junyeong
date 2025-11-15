package junyeong_spring.short_url_service.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface ShortenUrlRepository {

    void saveShortenUrl(ShortenUrl shortenUrl);
    ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey);
}
