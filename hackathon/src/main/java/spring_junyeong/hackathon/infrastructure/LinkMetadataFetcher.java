package spring_junyeong.hackathon.infrastructure;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.global.exception.LinkMetadataFetchException;
import spring_junyeong.hackathon.presentation.link.dto.LinkMetadata;

@Service
public class LinkMetadataFetcher {

  private static final int TIMEOUT_MILLIS = 5000;

  public LinkMetadata fetchMetadata(String url) {
    try {
      Connection connection = Jsoup.connect(url).timeout(TIMEOUT_MILLIS);
      Document document = connection.get();

      String title = getMetaContent(document, "og:title");
      String description = getMetaContent(document, "og:description");
      String imageSource = getMetaContent(document, "og:image");

      if (title.isBlank()) {
        title = document.title();
      }
      if (description.isBlank()) {
        description = getMetaContent(document, "description");
      }

      return new LinkMetadata(title, imageSource, description);

    } catch (IOException e) {
      throw new LinkMetadataFetchException("URL 메타데이터를 가져올 수 없습니다: " + url, e);
    }
  }

  public void isUrlReachable(String url) {
    try {
      Connection.Response response = Jsoup.connect(url)
          .method(Connection.Method.GET)
          .ignoreContentType(true)
          .timeout(TIMEOUT_MILLIS)
          .execute();

      if (response.statusCode() / 100 != 2) {
        throw new LinkMetadataFetchException(
            "URL에 접속할 수 없습니다. HTTP 상태 코드: " + response.statusCode());
      }

    } catch (IOException e) {
      throw new LinkMetadataFetchException("URL 접속 중 네트워크 오류가 발생했습니다: " + url, e);
    }
  }

  private String getMetaContent(Document document, String property) {
    Element meta = document.select("meta[property=" + property + "]").first();
    if (meta == null) {
      meta = document.select("meta[name=" + property + "]").first();
    }

    if (meta != null) {
      String content = meta.attr("content");
      return content != null ? content.trim() : "";
    }
    return "";
  }
}