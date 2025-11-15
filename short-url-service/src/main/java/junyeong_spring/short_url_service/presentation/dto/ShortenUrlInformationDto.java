package junyeong_spring.short_url_service.presentation.dto;

import junyeong_spring.short_url_service.domain.ShortenUrl;
import lombok.Getter;

@Getter
public class ShortenUrlInformationDto {
    private String originalUrl;
    private String shortenUrlKey;
    private Long redirectCount;

    public ShortenUrlInformationDto(ShortenUrl shortenUrl){
        this.originalUrl = shortenUrl.getOriginalUrl();
        this.shortenUrlKey = shortenUrl.getShortenUrlKey();
        this.redirectCount =  shortenUrl.getRedirectCount();
    }
}
