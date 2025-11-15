package spring_junyeong.hackathon.application;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import spring_junyeong.hackathon.domain.Folder;
import spring_junyeong.hackathon.domain.Link;
import spring_junyeong.hackathon.infrastructure.FolderRepository;
import spring_junyeong.hackathon.infrastructure.LinkMetadataFetcher;
import spring_junyeong.hackathon.infrastructure.LinkRepository;
import spring_junyeong.hackathon.presentation.link.dto.LinkMetadata;
import spring_junyeong.hackathon.presentation.link.dto.LinkRequest;
import spring_junyeong.hackathon.presentation.link.dto.LinkResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinksResponse;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkMetadataFetcher linkMetadataFetcher;
    private final FolderRepository folderRepository;

    public LinkResponse createLink(LinkRequest linkCreateRequest) {
        String url = linkCreateRequest.getUrl();
        linkMetadataFetcher.isUrlReachable(url);
        LinkMetadata linkData = linkMetadataFetcher.fetchMetadata(url);

        Link link = new Link(linkCreateRequest.getUrl(), linkData.getTitle(), linkData.getImageSource(),
                linkData.getDescription());
        Folder folder = folderRepository.findById(linkCreateRequest.getFolderId()).orElseThrow();

        folder.addLink(link);

        return new LinkResponse(link);

    }

    public LinksResponse getAllLinks() {
        List<Link> links = linkRepository.findAll();

        return new LinksResponse(links);
    }

    public LinksResponse getLinksInFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow();
        List<Link> list = folder.getLinks();

        return new LinksResponse(list);
    }

    public List<LinkResponse> getFavoriteLinks() {
        return linkRepository.getFavorites().stream().map(link -> new LinkResponse(link)).toList();
    }

    public LinkResponse updateLink(Long linkId, String url) {
        Link link = linkRepository.findById(linkId).orElseThrow();

        linkMetadataFetcher.isUrlReachable(url);

        link.updateUrl(url);

        return new LinkResponse(link);
    }

    public void removeLink(Long linkId) {
        linkRepository.remove(linkId);
    }

    public LinkResponse toggleLinkFavorite(Long linkId) {
        Link link = linkRepository.findById(linkId).orElseThrow();
        link.toggleFavorite();

        return new LinkResponse(link);
    }

}
