package spring_junyeong.hackathon.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;

@Getter
public class Folder {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void removeLink(Long linkId) {
        Link removeLink = findLinkById(linkId);
        links.remove(removeLink);
    }

    private Link findLinkById(Long linkId) {
        return links.stream().filter(link -> link.getId().equals(linkId)).findFirst().orElseThrow();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Folder other = (Folder) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
