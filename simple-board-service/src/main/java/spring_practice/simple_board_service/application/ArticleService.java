package spring_practice.simple_board_service.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.domain.Article;
import spring_practice.simple_board_service.domain.ArticleRepository;
import spring_practice.simple_board_service.domain.User;
import spring_practice.simple_board_service.domain.UserRepository;
import spring_practice.simple_board_service.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.exception.UserNotFoundException;
import spring_practice.simple_board_service.presentation.article.ArticleCreateRequest;
import spring_practice.simple_board_service.presentation.article.ArticleResponse;
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
    Article article = articleRepository.findById(articleId).orElseThrow(()-> new ArticleNotFoundException(articleId));

    return new ArticleResponse(article);
  }

  // 게시글 생성
  public ArticleResponse createArticle(ArticleCreateRequest request) {
    // id에 해당하는 user가 있는지부터 확인
    User user = userRepository.findByUserId(request.getUserId())
        .orElseThrow(UserNotFoundException::new);

    // 2. request에 대한 데이터로 article을 서버에서 생성 -> 이 때에는 id가 없어야 함, DB에서 가져와야 함(ex.KeyHolder)
    Article article = new Article(request);

    // 3. article 영속성 관리 ( 어플리케이션 데이터 -> DB 데이터 )
    article = articleRepository.add(article);

    // 4. 비로소 DB와 똑같아진 article을 Dto화 하여 return -> 서버에 생성된 article을 client에게 확인시켜줘야 함
    return new ArticleResponse(article);

  }

  // 게시글 수정
  public ArticleResponse updateArticle(ArticleUpdateRequest request) {
    // id에 해당하는 user가 있는지부터 확인
    User user = userRepository.findByUserId(request.getUserId)
        .orElseThrow(() -> new UserNotFoundException());

    // 2. 새로운 request로 article을 새로 만듦 -> 기존에 article에 덮어쓰기 위함, 부분 업데이트라면?
    Article article = new Article(request);

    // 3. article 영속성 관리 ( 어플리케이션 데이터 -> DB 데이터 )
    article = articleRepository.update(user.getId(), article);

    // 4. 비로소 DB와 똑같아진 article을 Dto화 하여 return -> 서버에 생성된 article을 client에게 확인시켜줘야 함
    return new ArticleResponse(article);


  }

  // 게시글 삭제
  public void deleteArticle(Long userId, Long articleId) {
    // 1. 기존에 article 가져옴
    Article article = articleRepository.findById(articleId);

    // 2. 생성시 article에 할당된 작성자 id와 http 요청을 보낸 id가 같은지 확인 -> 게시글을 지울 권한이 있는지 확인
    Boolean isAuthorEqual = article.isAuthorIdEqaul(userId);

    // 3. 있다면 delete, 없으면 예외 ("게시글을 삭제할 권한이 없습니다.")
    if (isAuthorEqual) {
      articleRepository.delete(articleId);
    }else {
      throw new 유효하지않은인가예외
    }
  }

}
