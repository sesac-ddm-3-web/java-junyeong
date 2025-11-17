package spring_junyeong.hackathon.presentation.user.dto;

import lombok.Getter;

@Getter
public class CheckEmailResponse {

  private boolean isUsableEmail;

  public CheckEmailResponse(boolean isUsableEmail) {
    this.isUsableEmail = isUsableEmail;
  }
}
