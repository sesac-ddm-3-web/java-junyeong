package junyeong_spring.short_url_service.application;

import junyeong_spring.short_url_service.domain.ShortenUrl;
import junyeong_spring.short_url_service.domain.ShortenUrlRepository;
import junyeong_spring.short_url_service.global.exception.LackOfShortenUrlKeyException;
import junyeong_spring.short_url_service.global.exception.NotFoundShortenUrlException;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlCreateReponseDto;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlCreateRequestDto;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlInformationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;

    public ShortenUrlCreateReponseDto generateShortenUrl(
            ShortenUrlCreateRequestDto shortenUrlCreateRequestDto
    ){
        String originalUrl = shortenUrlCreateRequestDto.getOriginalUrl();
        String shortenUrlKey = getUniqueShortenUrlKey();
        ShortenUrl shortenUrl = new ShortenUrl(originalUrl, shortenUrlKey);

        shortenUrlRepository.saveShortenUrl(shortenUrl);

        ShortenUrlCreateReponseDto shortenUrlCreateReponseDto = new ShortenUrlCreateReponseDto(shortenUrl);

        return shortenUrlCreateReponseDto;
    }

    public ShortenUrlInformationDto getShortenUrlInformationByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

        if(null == shortenUrl){
            throw new NotFoundShortenUrlException();
        }

        ShortenUrlInformationDto shortenUrlInformationDto = new ShortenUrlInformationDto(shortenUrl);

        return shortenUrlInformationDto;

    }

    public String getoriginalUrlByShortenUrlKey(String ShortenUrlKey){
        ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(ShortenUrlKey);

        if(null == shortenUrl){
            throw new NotFoundShortenUrlException();
        }

        shortenUrl.increaseRedirectCount();

        shortenUrlRepository.saveShortenUrl(shortenUrl);

        String originalurl = shortenUrl.getOriginalUrl();

        return originalurl;
    }

    private String getUniqueShortenUrlKey(){
                final int MAX_RETRY_COUNT = 5;
        int count = 0;

        while(count++ < MAX_RETRY_COUNT){
            String shortenUrlKey = ShortenUrl.generateShortenUrlKey();

            ShortenUrl shortenUrl = shortenUrlRepository.findShortenUrlByShortenUrlKey(shortenUrlKey);

            if(null == shortenUrl)
                return shortenUrlKey;
        }

        throw new LackOfShortenUrlKeyException();
    }
}
