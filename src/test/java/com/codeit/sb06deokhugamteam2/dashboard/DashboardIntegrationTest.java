package com.codeit.sb06deokhugamteam2.dashboard;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.book.fixture.BookFixture;
import com.codeit.sb06deokhugamteam2.dashboard.dto.response.CursorPageResponsePopularReviewDto;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.dashboard.fixture.DashboardFixture;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLike;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DashboardIntegrationTest.class)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
public class DashboardIntegrationTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("인기 리뷰 조회 통합 테스트")
    void findPopularReviews_Success() throws Exception {
        int count = 1;
        Book book = BookFixture.createBook(count);
        em.persist(book);
        Review review = DashboardFixture.createReview(count, book);
        User user = DashboardFixture.createUser(count, List.of(review));
        em.persist(user);
        ReviewLike reviewLike = DashboardFixture.createReviewLike(review, user); //없어도됌
        em.persist(reviewLike);
        Dashboard dashboard = DashboardFixture.createDashboard(1, review.id());
        em.persist(dashboard);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/books/popular"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        String body = result.getResponse().getContentAsString();
        System.out.println(body);
        CursorPageResponsePopularReviewDto dto = objectMapper.readValue(body, CursorPageResponsePopularReviewDto.class);

        assertThat(dto.getContent()).isNotNull();
        assertThat(dto.getTotalElements()).isEqualTo(1);
        assertThat(dto.getSize()).isEqualTo(dto.getContent().size());
        assertThat(dto.getNextCursor()).isNotNull();
        assertThat(dto.getNextAfter()).isNotNull();
    }
}
