package spring_practice.simple_board_service.application.article;

import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.common.exception.UserNotFoundException;
import spring_practice.simple_board_service.domain.article.Article;
import spring_practice.simple_board_service.domain.article.ArticleRepository;
import spring_practice.simple_board_service.domain.auth.AuthenticationContext;
import spring_practice.simple_board_service.domain.auth.User;
import spring_practice.simple_board_service.domain.auth.UserPrincipal;
import spring_practice.simple_board_service.domain.auth.UserRepository;
import spring_practice.simple_board_service.presentation.article.ArticleCreateRequest;
import spring_practice.simple_board_service.presentation.article.ArticleResponse;
import spring_practice.simple_board_service.presentation.article.ArticleUpdateRequest;
import spring_practice.simple_board_service.presentation.article.ArticlesResponse;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  // 게시글 전체 조회 (검색어 조회 가능)
  public ArticlesResponse getArticles(String search) {
    List<Article> articles;

    if (search == null) {
      articles = articleRepository.findAll();
    } else {
      articles = articleRepository.findBySearchKeyword(search);
    }

    return new ArticlesResponse(articles.stream().map(ArticleResponse::new).toList());

  }

  // id에 해당하는 게시글 조회
  public ArticleResponse getArticleById(Long articleId) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    return new ArticleResponse(article);
  }

  // 게시글 생성
  public ArticleResponse createArticle(ArticleCreateRequest request) {
    // id에 해당하는 user가 있는지부터 확인
    User user = userRepository.findByUserId(request.getUserId())
        .orElseThrow(UserNotFoundException::new);

    // 2. request에 대한 데이터로 article을 서버에서 생성 -> 이 때에는 id가 없어야 함, DB에서 가져와야 함(ex.KeyHolder)
    Article article = request.toEntity();

    // 3. article 영속성 관리 ( 어플리케이션 데이터 -> DB 데이터 )
    article = articleRepository.add(request.toEntity());

    // 4. 비로소 DB와 똑같아진 article을 Dto화 하여 return -> 서버에 생성된 article을 client에게 확인시켜줘야 함
    return new ArticleResponse(article);

  }

  // 게시글 수정
  public ArticleResponse updateArticle(ArticleUpdateRequest request) {

    Long updateArticleId = request.getArticleId();

    // 1. id에 해당하는 article이 있는지부터 확인
    Article article = articleRepository.findById(updateArticleId)
        .orElseThrow(() -> new ArticleNotFoundException(updateArticleId));

    // 2. domain entity를 변경 -> repo에서 하지 않고, service에서 article을 새로 만들어서 보냄 -> repo는 영속성 관리만 (바뀐 데이터를 저장만, 데이터를 어떻게 바꾸는지는 service)
    article.update(request.getTitle(), request.getContent());

    // 2. 바뀐 article 영속성 관리 ( 어플리케이션 데이터 -> DB 데이터 )
    article = articleRepository.update(updateArticleId, article);

    // 4. 비로소 DB와 똑같아진 article을 Dto화 하여 return -> 서버에 생성된 article을 client에게 확인시켜줘야 함
    return new ArticleResponse(article);


  }

  // 게시글 삭제
  public void deleteArticle(Long articleId) throws AccessDeniedException {

    // 1. 인증 정보가 없을 경우 (필터에서 차단되지 않고 여기까지 온 경우) 예외 발생
    UserPrincipal principal = AuthenticationContext.getPrincipal();
    if (principal == null) {
      throw new AccessDeniedException("인증 정보가 없습니다.");
    }

    Long currentUserId = principal.getUserId();

    // 2. 기존에 article 가져옴 -> null일시 예외던짐
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    // 3. 생성시 article에 할당된 작성자 id와 요청을 보낸 id가 같은지 확인 -> 게시글을 지울 권한이 있는지 확인
    Boolean isAuthorEqual = article.isAuthorEqual(currentUserId);

    // 4. 있다면 delete, 없으면 예외 ("게시글을 삭제할 권한이 없습니다.")
    if (isAuthorEqual) {
      articleRepository.delete(articleId);
    } else {
      throw new AccessDeniedException("게시글을 지울 권한이 없습니다.");
    }
  }

}
