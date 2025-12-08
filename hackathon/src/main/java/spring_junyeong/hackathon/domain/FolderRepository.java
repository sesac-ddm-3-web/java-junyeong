package spring_junyeong.hackathon.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {

  Optional<Folder> findByName(String name);

  Optional<Folder> findById(Long id);

  Boolean existsByName(String name);

}
