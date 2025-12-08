package spring_junyeong.hackathon.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "folder")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  // 💡 JPA가 관리하지 않는 필드 (DB 컬럼으로 매핑되지 않음)
  // Link 개수는 JPA 연관관계가 관리하므로 이 필드는 제거하거나 @Transient 처리합니다.
  @Transient
  private Long linkCount;

  // 💡 연관 관계 매핑: Folder(1) <-> Link(N)
  // mappedBy="folder": 연관 관계의 주인이 아님을 명시 (Link 엔티티의 folder 필드에 의해 매핑됨)
  // CascadeType.ALL: Folder가 삭제되면 포함된 Link도 함께 삭제되도록 설정
  @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Link> links = new ArrayList<>();

  @Builder
  public Folder(String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
    this.linkCount = 0L; // transient 필드 초기화
  }

  public void rename(String newName) {
    this.name = newName;
  }

  public void addLink(Link link) {
    links.add(link);
    if (link.getFolder() != this) {
      link.setFolder(this);
    }
  }

  public void removeLink(Link link) {
    links.remove(link);
    link.setFolder(null);
  }


  public long getLinkCount() {
    return links.size();
  }
}