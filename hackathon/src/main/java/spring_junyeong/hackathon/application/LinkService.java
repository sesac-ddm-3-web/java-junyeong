package spring_junyeong.hackathon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.Folder;
import spring_junyeong.hackathon.domain.Link;
import spring_junyeong.hackathon.global.exception.FolderNotFoundException;
import spring_junyeong.hackathon.global.exception.LinkNotFoundException;
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

    Folder folder = folderRepository.findById(linkCreateRequest.getFolderId())
        .orElseThrow(() -> new FolderNotFoundException(linkCreateRequest.getFolderId()));

    folder.addLink(link);

    return new LinkResponse(link);

  }

  public LinksResponse getAllLinks(int page, int pageSize, String search) {
    long offset = (long) page * pageSize;

    List<Folder> folders = folderRepository.findAll();
    List<Link> links = search == null
        ? folders.stream().flatMap(folder -> folder.getLinks().stream()).toList()
        : folders.stream().flatMap(folder -> folder.getLinks().stream())
            .filter(link -> link.isKeywordExist(search)).toList();

    List<Link> filteredLinks = links.stream()
        .skip(offset)
        .limit(pageSize)
        .toList();

    return new LinksResponse(filteredLinks);
  }

  public LinksResponse getLinksInFolder(Long folderId) {
    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new FolderNotFoundException(folderId));

    List<Link> list = folder.getLinks();

    return new LinksResponse(list);
  }

  public List<LinkResponse> getFavoriteLinks() {
    return linkRepository.getFavorites().stream().map(LinkResponse::new).toList();
  }

  public LinkResponse updateLink(Long linkId, String url) {
    Link link = linkRepository.findById(linkId)
        .orElseThrow(() -> new LinkNotFoundException(linkId));

    linkMetadataFetcher.isUrlReachable(url);

    link.updateUrl(url);

    return new LinkResponse(link);
  }

  public void removeLink(Long linkId) {
    Link removeLink = linkRepository.findById(linkId)
        .orElseThrow(() -> new LinkNotFoundException(linkId));

    linkRepository.remove(linkId);
  }

  public LinkResponse toggleLinkFavorite(Long linkId) {
    Link link = linkRepository.findById(linkId)
        .orElseThrow(() -> new LinkNotFoundException(linkId));

    link.toggleFavorite();

    return new LinkResponse(link);
  }

}
