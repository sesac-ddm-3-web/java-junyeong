package spring_junyeong.hackathon.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_junyeong.hackathon.domain.Folder;
import spring_junyeong.hackathon.domain.FolderRepository;
import spring_junyeong.hackathon.domain.Link;
import spring_junyeong.hackathon.domain.LinkRepository;
import spring_junyeong.hackathon.global.exception.FolderNotFoundException;
import spring_junyeong.hackathon.global.exception.LinkNotFoundException;
import spring_junyeong.hackathon.infrastructure.LinkMetadataFetcher;
import spring_junyeong.hackathon.presentation.link.dto.LinkMetadata;
import spring_junyeong.hackathon.presentation.link.dto.LinkRequest;
import spring_junyeong.hackathon.presentation.link.dto.LinkResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinksResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

  private final LinkRepository linkRepository;
  private final LinkMetadataFetcher linkMetadataFetcher;
  private final FolderRepository folderRepository;

  @Transactional
  public LinkResponse createLink(LinkRequest linkCreateRequest) {
    String url = linkCreateRequest.getUrl();
    linkMetadataFetcher.isUrlReachable(url);
    LinkMetadata linkData = linkMetadataFetcher.fetchMetadata(url);

    Folder folder = folderRepository.findById(linkCreateRequest.getFolderId())
        .orElseThrow(() -> new FolderNotFoundException(linkCreateRequest.getFolderId()));

    Link link = Link.builder()
        .url(linkCreateRequest.getUrl())
        .title(linkData.getTitle())
        .imageSource(linkData.getImageSource())
        .description(linkData.getDescription())
        .folder(folder) // ManyToOne 관계 설정
        .build();

    folder.addLink(link);

    linkRepository.save(link);

    return new LinkResponse(link);
  }

  public LinksResponse getAllLinks(int page, int pageSize, String search) {
    Pageable pageable = PageRequest.of(page, pageSize);
    Page<Link> linkPage;

    if (search != null && !search.isBlank()) {
      // 💡 LinkRepository의 쿼리 메서드를 사용하여 DB에서 검색 및 페이징 처리
      linkPage = linkRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
          search, search, pageable);
    } else {
      // 💡 검색어가 없는 경우 전체 조회 및 페이징 처리
      linkPage = linkRepository.findAll(pageable);
    }

    return new LinksResponse(linkPage.getContent());
  }

  public LinksResponse getLinksInFolder(Long folderId) {
    List<Link> list = linkRepository.findAllByFolderId(folderId);

    return new LinksResponse(list);
  }

  public List<LinkResponse> getFavoriteLinks() {
    return linkRepository.findAllByFavoriteTrue().stream()
        .map(LinkResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public LinkResponse updateLink(Long linkId, String url) {
    Link link = linkRepository.findById(linkId)
        .orElseThrow(() -> new LinkNotFoundException(linkId));

    linkMetadataFetcher.isUrlReachable(url);

    // 💡 상태 변경 후 트랜잭션 종료 시 자동 업데이트 (Dirty Checking)
    link.updateUrl(url);

    return new LinkResponse(link);
  }

  @Transactional
  public void removeLink(Long linkId) {
    // 💡 삭제 전 존재 여부를 확인하여 명시적인 예외를 던집니다.
    if (!linkRepository.existsById(linkId)) {
      throw new LinkNotFoundException(linkId);
    }
    linkRepository.deleteById(linkId);
  }

  @Transactional
  public LinkResponse toggleLinkFavorite(Long linkId) {
    Link link = linkRepository.findById(linkId)
        .orElseThrow(() -> new LinkNotFoundException(linkId));

    link.toggleFavorite();

    return new LinkResponse(link);
  }
}