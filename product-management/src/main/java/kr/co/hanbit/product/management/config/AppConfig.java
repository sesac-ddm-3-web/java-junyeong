package kr.co.hanbit.product.management.config; // config 패키지를 추가하면 더 좋습니다.

import java.sql.Connection;
import javax.sql.DataSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@org.springframework.context.annotation.Configuration
public class AppConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        .setFieldMatchingEnabled(true);
    return modelMapper;
  }

  @Bean
  @Profile("prod")
  public ApplicationRunner runner(DataSource dataSource) {
    return args -> {
      Connection connection = dataSource.getConnection();
    };
  }
}