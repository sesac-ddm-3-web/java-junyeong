package spring_junyeong.hackathon.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {

  List<Link> findAllByFolderId(Long folderId);

  List<Link> findAllByFavoriteTrue();

  Page<Link> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String title,
      String description,
      Pageable pageable
  );
}