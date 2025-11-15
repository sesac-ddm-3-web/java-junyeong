package spring_junyeong.hackathon.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring_junyeong.hackathon.application.LinkService;
import spring_junyeong.hackathon.presentation.link.dto.LinkResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinkUpdateRequest;
import spring_junyeong.hackathon.presentation.link.dto.LinksResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinkRequest;

@RestController
@RequiredArgsConstructor
public class LinkRestController {

    private final LinkService linkService;

    @RequestMapping(value = "/links", method = RequestMethod.GET)
    public ResponseEntity<LinksResponse> getLinks() {

        LinksResponse response = linkService.getAllLinks();
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public ResponseEntity<List<LinkResponse>> getFavorites() {
        List<LinkResponse> response = linkService.getFavoriteLinks();

        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "/folders/{folderId}/links", method = RequestMethod.GET)
    public ResponseEntity<LinksResponse> getLinksInFolder(@PathVariable Long folderId) {
        LinksResponse response = linkService.getLinksInFolder(folderId);

        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(value = "/links", method = RequestMethod.POST)
    public ResponseEntity<LinkResponse> createLink(@Valid @RequestBody LinkRequest request) {
        LinkResponse response = linkService.createLink(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/links/{linkId}", method = RequestMethod.PUT)
    public ResponseEntity<LinkResponse> updateLink(@PathVariable Long linkId, @RequestBody LinkUpdateRequest request) {
        LinkResponse response = linkService.updateLink(linkId, request.getUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/links/{linkId}/favorite", method = RequestMethod.PUT)
    public ResponseEntity<LinkResponse> toggleFavoriteLink(@PathVariable Long linkId) {
        LinkResponse response = linkService.toggleLinkFavorite(linkId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/links/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeLink(@PathVariable Long linkId) {
        linkService.removeLink(linkId);

        return ResponseEntity.noContent().build();
    }

}
