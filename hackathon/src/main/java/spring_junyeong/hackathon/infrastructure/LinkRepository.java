package spring_junyeong.hackathon.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import spring_junyeong.hackathon.domain.Link;

@Repository
@RequiredArgsConstructor
public class LinkRepository {
    private List<Link> links = new CopyOnWriteArrayList<>();
    private AtomicLong sequence = new AtomicLong(0L);

    public Link save(Link link, String folderName) {
        link.setId(sequence.incrementAndGet());

        links.add(link);

        return link;
    }

    public Optional<Link> findById(Long id) {
        return links.stream().filter(link -> link.getId().equals(id)).findFirst();
    }

    public List<Link> findAll() {
        return links;
    }

    public Link remove(Long linkId) {
        Link link = findById(linkId).orElseThrow();
        links.remove(link);

        return link;
    }

    public List<Link> getFavorites() {
        return links.stream().filter(link -> link.isFavorite()).toList();
    }

}
