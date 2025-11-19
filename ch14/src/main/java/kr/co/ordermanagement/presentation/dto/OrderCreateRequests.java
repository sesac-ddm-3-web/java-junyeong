package kr.co.ordermanagement.presentation.dto;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequests {

  @Valid
  private List<OrderCreateRequest> orders;

}
