package spring_junyeong.hackathon.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import spring_junyeong.hackathon.domain.Folder;

@Repository
public class FolderRepository {
    private final Map<Long, Folder> folders = new HashMap<>();
    private AtomicLong sequence = new AtomicLong(0L);

    public Folder add(Folder folder){
        folder.setId(sequence.incrementAndGet());
        folders.put(folder.getId(), folder);

        return folder;
    }

    public List<Folder> findAll() {
        return List.copyOf(folders.values());
    }

    public Optional<Folder> findById(Long id){
        return Optional.ofNullable(folders.get(id));
    }

    public Optional<Folder> findByName(String name) {
        return folders.values().stream().filter(folder -> folder.getName().equals(name)).findFirst();
    }

    public void delete(Long id){
        folders.remove(id);
    }
}
