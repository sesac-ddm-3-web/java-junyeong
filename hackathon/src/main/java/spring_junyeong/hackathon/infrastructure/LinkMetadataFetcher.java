package spring_junyeong.hackathon.infrastructure;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import spring_junyeong.hackathon.global.exception.ConnectionException;
import spring_junyeong.hackathon.presentation.link.dto.LinkMetadata;

@Component
public class LinkMetadataFetcher {
    private static final int SUCCESS_MIN_STATUS = 200;
    private static final int SUCCESS_MAX_STATUS = 399;


    public boolean isUrlReachable(String urlStr) {
        try {
            Connection.Response response = Jsoup.connect(urlStr)
                    .timeout(5000) 
                    .method(Connection.Method.HEAD) // GET 대신 HEAD 요청을 사용 (본문 데이터를 받지 않아 빠름)
                    .ignoreHttpErrors(true) // 4xx, 5xx 에러 발생 시에도 예외를 던지지 않고 상태 코드를 받을 수 있게 설정
                    .execute();
            
            int statusCode = response.statusCode();
            
            return statusCode >= SUCCESS_MIN_STATUS && statusCode <= SUCCESS_MAX_STATUS;
            
        } catch (IOException e) {
          throw new ConnectionException("URL 통신에 실패했습니다.", e);
        } catch (IllegalArgumentException e) {
          throw new ConnectionException("유효하지 않은 URL 형식입니다.", e);
        }
    }


    public LinkMetadata fetchMetadata(String url) {

        try{
            Document doc = Jsoup.connect(url)
                    .timeout(5000)
                    .header("User-Agent", "Mozilla/5.0") // 봇으로 인식되지 않도록 User-Agent 설정
                    .get();

            String title = doc.title();

            String description = getOgProperty(doc, "og:description");
            if (description.isEmpty()) {
                description = getMetaTagContent(doc, "description");
            }

            String imageSource = getOgProperty(doc, "og:image");

            return new LinkMetadata(title, description, imageSource);
        } catch (IOException e){
            throw new ConnectionException("URL에 접근할 수 없거나 메타데이터 파싱에 실패했습니다.", e);
        }

    }

    private String getOgProperty(Document doc, String property) {
        Element element = doc.selectFirst("meta[property=" + property + "]");
        return element != null ? element.attr("content") : "";
    }

    private String getMetaTagContent(Document doc, String name) {
        Element element = doc.selectFirst("meta[name=" + name + "]");
        return element != null ? element.attr("content") : "";
    }


}
