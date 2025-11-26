package kr.co.shortenurlservice.infrastructure;

import java.sql.PreparedStatement;
import java.util.List;
import kr.co.shortenurlservice.domain.LackOfShortenUrlKeyException;
import kr.co.shortenurlservice.domain.ShortenUrl;
import kr.co.shortenurlservice.domain.ShortenUrlRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jdbc")
public class JdbcShortenUrlRepository implements ShortenUrlRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcShortenUrlRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<ShortenUrl> shortenUrlRowMapper = (rs, rowNum) -> {
    return new ShortenUrl(rs.getString("original_url"),
        rs.getString("shorten_url"),
        rs.getLong("redirect_count"));
  };

  @Override
  public void saveShortenUrl(ShortenUrl shortenUrl) {
    String sql = "INSERT INTO shorten_url (original_url, shorten_url, redirect_count) VALUES (?, ?, ?)";

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, shortenUrl.getOriginalUrl());
      ps.setString(2, shortenUrl.getShortenUrlKey());
      ps.setLong(3, shortenUrl.getRedirectCount());
      return ps;
    });
  }

  @Override
  public ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey) {
    String sql = "SELECT * FROM shorten_url WHERE shorten_url = ?";

    List<ShortenUrl> urls = jdbcTemplate.query(sql, shortenUrlRowMapper, shortenUrlKey);

    if (urls.isEmpty()) {
      throw new LackOfShortenUrlKeyException();
    }
    
    return urls.get(0);
  }
}
