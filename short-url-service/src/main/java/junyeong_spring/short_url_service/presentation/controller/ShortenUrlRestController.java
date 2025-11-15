package junyeong_spring.short_url_service.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import junyeong_spring.short_url_service.application.SimpleShortenUrlService;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlCreateReponseDto;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlCreateRequestDto;
import junyeong_spring.short_url_service.presentation.dto.ShortenUrlInformationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Tag(name = "Short URL API", description = "단축 URL의 생성, 리다이렉션, 정보 조회 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
public class ShortenUrlRestController {

    private final SimpleShortenUrlService simpleShortenUrlService;

    @Operation(
            summary = "단축 URL 생성",
            description = "긴 원본 URL을 받아 고유한 단축 키를 생성하고, 결과를 반환합니다."
    )
    @RequestMapping(value = "/shorten-url", method = RequestMethod.POST)
    public ResponseEntity<ShortenUrlCreateReponseDto> createShortenUrl(
            @Valid @RequestBody ShortenUrlCreateRequestDto shortenUrlCreateRequestDto
    ){
        ShortenUrlCreateReponseDto shortenUrlCreateReponseDto =
                simpleShortenUrlService.generateShortenUrl(shortenUrlCreateRequestDto);

        return ResponseEntity.ok(shortenUrlCreateReponseDto);
    }

    @Operation(
            summary = "단축 URL 리다이렉션",
            description = "단축 키를 통해 저장된 원본 URL로 사용자를 리다이렉트합니다."
    )
    @RequestMapping(value = "/redirect/{shortenUrlKey}", method = RequestMethod.GET)
    public ResponseEntity<?> redirectShortenUrl(
            @PathVariable String shortenUrlKey
    ) throws URISyntaxException {
        String originalUrl = simpleShortenUrlService.getoriginalUrlByShortenUrlKey(shortenUrlKey);

        URI redirectUri = new URI(originalUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(
            summary = "단축 URL 정보 조회",
            description = "특정 단축 키의 원본 URL, 생성 시각, 클릭 횟수 등 상세 정보를 조회합니다."
    )
    @RequestMapping(value = "/shorten-url/{shortenUrlKey}", method = RequestMethod.GET)
    public ResponseEntity<ShortenUrlInformationDto> getShortenUrlInformation(
            @PathVariable String shortenUrlKey
    ){
        ShortenUrlInformationDto shortenUrlInformationDto =
                simpleShortenUrlService.getShortenUrlInformationByShortenUrlKey(shortenUrlKey);

        return ResponseEntity.ok(shortenUrlInformationDto);
    }

}
