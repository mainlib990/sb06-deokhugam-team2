package com.codeit.sb06deokhugamteam2;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb06deokhugamteam2.notification.NotificationComponent;
import com.codeit.sb06deokhugamteam2.notification.entity.dto.NotificationDto;
import com.codeit.sb06deokhugamteam2.notification.entity.dto.request.NotificationCreateRequest;
import java.lang.annotation.Documented;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NotificationTest {

  @Autowired
  private NotificationComponent notificationComponent;

  @Test
  @DisplayName("Notification 저장 성공 테스트")
  public void saveNotification() {
    NotificationCreateRequest request = new NotificationCreateRequest(UUID.randomUUID()
        ,UUID.randomUUID()
        ,"title"
        ,"content");

    NotificationDto dto = notificationComponent.saveNotification(request);
    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isNotNull();
    assertThat(dto.getReviewId()).isNotNull();
    assertThat(dto.getCreatedAt()).isNotNull();
    assertThat(dto.getUpdatedAt()).isNotNull();
    assertThat(dto.getConfirmed()).isEqualTo(false);
  }
}
