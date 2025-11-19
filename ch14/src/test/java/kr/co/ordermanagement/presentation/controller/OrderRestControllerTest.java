package kr.co.ordermanagement.presentation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // print() 사용을 위해 추가

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.ordermanagement.application.SimpleOrderService;
import kr.co.ordermanagement.presentation.dto.OrderCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest // 통합 테스트 환경 설정
@AutoConfigureMockMvc // MockMvc 자동 구성
class OrderRestControllerTest {

  @MockBean
  private SimpleOrderService simpleOrderService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("상품 주문 시, List 내의 한 요청이라도 유효성 검사에 실패하면 400 응답을 반환한다.")
  void fail_to_create_order_when_request_is_invalid() throws Exception {

    // 1. 유효한 요청 데이터
    OrderCreateRequest validRequest = OrderCreateRequest.builder()
        .id(1L) // DTO 필드명 'id' 사용
        .amount(5) // DTO 필드명 'amount' 사용, 유효 (1 <= 5 <= 50)
        .build();

    // 2. 유효하지 않은 요청 데이터 (amount가 @Min(1)을 위반)
    OrderCreateRequest invalidRequest = OrderCreateRequest.builder()
        .id(2L)
        .amount(0) // 유효성 검사 실패 지점: @Min(1) 위반
        .build();

    // 3. 요청 List 생성
    List<OrderCreateRequest> requestList = List.of(validRequest, invalidRequest);

    // 4. 요청 실행
    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestList)))

        // 5. 검증: HTTP 상태 코드가 400 Bad Request인지 확인
        .andExpect(status().isBadRequest())

        // 6. 검증: 응답 본문에 유효성 검사 실패 메시지가 포함되어 있는지 확인
        // DTO의 메시지: "주문 수량은 1개 이상이어야 합니다."
        .andExpect(jsonPath("$.message").value("주문 수량은 1개 이상이어야 합니다."))
        .andDo(print()); // 요청/응답 상세 내용을 콘솔에 출력
  }
}