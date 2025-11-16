package spring_junyeong.hackathon.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Folder {
    @Setter
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Long linkCount;
    private List<Link> links = new CopyOnWriteArrayList<>();

    public Folder(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.linkCount = 0L;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void addLink(Link link) {
        links.add(link);
        this.linkCount++;
    }

    public void removeLink(Long linkId) {
        Link removeLink = findLinkById(linkId);
        links.remove(removeLink);
    }

    private Link findLinkById(Long linkId) {
        return links.stream().filter(link -> link.getId().equals(linkId)).findFirst().orElseThrow();
    }
}
