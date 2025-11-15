package spring_junyeong.hackathon.presentation.link.dto;

import java.util.List;

import lombok.Getter;
import spring_junyeong.hackathon.domain.Link;

@Getter
public class LinksResponse {
    int totalCount;
    List<Link> list;

    public LinksResponse(List<Link> links) {
        this.totalCount = links.size();
        this.list = links;
    }

}
