package com.codeit.sb06deokhugamteam2.dashboard;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.fixture.BookFixture;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.dashboard.dto.response.CursorPageResponsePopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.dashboard.fixture.DashboardFixture;
import com.codeit.sb06deokhugamteam2.dashboard.repository.DashboardRepository;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DashboardIntegrationTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    DashboardRepository dashboardRepository;

    private final UUID HEADER_USER_ID = UUID.randomUUID();
    @Autowired
    private Job createPopularReviewJob;


    @Test
    @DisplayName("인기 리뷰 조회 통합 테스트")
    void findPopularReviews_Success() throws Exception {
        int number = 3;
        List<Book> books = BookFixture.createBooks(number);
        books.forEach(em::persist);
        List<Review> reviews = DashboardFixture.createReviews(number, books);
        List<User> users = DashboardFixture.createUsers(number, reviews);
        users.forEach(em::persist);
        List<UUID> reviewIds = reviews.stream().map(Review::id).toList();
        List<Dashboard> dashboards = DashboardFixture.createDashboards(number, reviewIds);
        dashboards.forEach(em::persist);
        em.flush();

        Instant dashboardCreatedAt = Instant.now().minus(1, ChronoUnit.DAYS);

        em.createQuery("UPDATE Dashboard d SET d.createdAt = :createdAt")
                .setParameter("createdAt", dashboardCreatedAt)
                .executeUpdate();
        em.clear();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/popular")
                        .header("Deokhugam-Request-User-ID", HEADER_USER_ID.toString()))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        String body = result.getResponse().getContentAsString();
        System.out.println(body);
        CursorPageResponsePopularReviewDto dto = objectMapper.readValue(body, CursorPageResponsePopularReviewDto.class);

        assertThat(dto.getContent()).isNotNull();
        assertThat(dto.getSize()).isEqualTo(dto.getContent().size());
        assertThat(dto.getTotalElements()).isEqualTo(3);
        assertThat(dto.getNextCursor()).isNull();
        assertThat(dto.getNextAfter()).isNull();
    }

    @Test
    @DisplayName("CreatePopularReviewJob 실행 테스트")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void popularReviewJob_Success() throws Exception {
            List<PeriodType> periods = List.of(PeriodType.DAILY, PeriodType.WEEKLY, PeriodType.MONTHLY, PeriodType.ALL_TIME);

            for(PeriodType period: periods) {
                JobParameters params = new JobParametersBuilder()
                        .addString("periodType", period.name())
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

                JobExecution execution = jobLauncher.run(createPopularReviewJob, params);

                assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
            }
    }

    @AfterEach
    void tearDown() {
        dashboardRepository.deleteAll();
    }

}
