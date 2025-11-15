package junyeong_spring.short_url_service.presentation.dto;

import junyeong_spring.short_url_service.domain.ShortenUrl;
import lombok.Getter;

@Getter
public class ShortenUrlCreateReponseDto {
    private String originalUrl;
    private String shortenUrlKey;

    public ShortenUrlCreateReponseDto(ShortenUrl shortenUrl){
        this.originalUrl = shortenUrl.getOriginalUrl();
        this.shortenUrlKey = shortenUrl.getShortenUrlKey();
    }

}
