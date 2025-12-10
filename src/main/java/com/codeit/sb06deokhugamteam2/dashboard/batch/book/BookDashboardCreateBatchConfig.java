package com.codeit.sb06deokhugamteam2.dashboard.batch.book;


import com.codeit.sb06deokhugamteam2.book.dto.data.BookDashboardDto;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@RequiredArgsConstructor
public class BookDashboardCreateBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;



    @Bean
    public Job createRankingBooksJob() {
        return new JobBuilder("createRankingBooksJob", jobRepository)
                .start(createDailyRankingBooksStep())
                .build();
    }

    @Bean
    public Step createDailyRankingBooksStep() {
        return new StepBuilder("createDailyRankingBooksStep", jobRepository)
                .<BookDashboardDto, Dashboard>chunk(100, transactionManager)
                .reader(createRankingBooksItemReader(null))
                .processor(createRankingBooksItemProcessor(null))
                .writer(createRankingBooksWriter())
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<BookDashboardDto> createRankingBooksItemReader(
            @Value("#{jobParameters['periodType']}") PeriodType periodType
    ) {

        LocalDateTime since = null;

        switch (periodType) {
            case DAILY -> since = LocalDate.now().atStartOfDay().minusDays(1);
            case WEEKLY -> since = LocalDate.now().atStartOfDay().minusDays(7);
            case MONTHLY -> since = LocalDate.now().atStartOfDay().minusMonths(1);
            case ALL_TIME -> since = LocalDateTime.of(1970, 1, 1, 0, 0);
        }

        /*
         1. 계산된 점수 기준 내림차순 정렬
         2. 점수가 같을 경우 도서의 생성일 기준 내림차순 정렬
         3. since 가 null 이면 전체 기간, 아니면 해당 기간 내의 리뷰만 집계
         4. 논리 삭제된 도서는 제외
         5. 리뷰가 없는 도서는 제외
         */
        String nativeQuery = """
                                SELECT b.id, b.created_at, COUNT(r.book_id) AS review_count, SUM(r.rating) AS rating_sum,
                                       (COUNT(r.book_id) * 0.4 + SUM(r.rating) / COUNT(r.book_id) * 0.6) AS score
                                FROM books as b
                                LEFT JOIN reviews as r ON b.id = r.book_id
                                AND r.created_at >= ?
                                WHERE b.deleted = false
                                GROUP BY b.id, b.created_at
                                HAVING COUNT(r.book_id) > 0
                                ORDER BY score DESC, b.created_at DESC
                """;

        final LocalDateTime finalSince = since;

        return new JdbcCursorItemReaderBuilder<BookDashboardDto>()
                .name("createRankingBooksItemReader")
                .dataSource(dataSource)
                .sql(nativeQuery)
                .preparedStatementSetter(ps ->
                    ps.setTimestamp(1, Timestamp.valueOf(finalSince))
                )
                .rowMapper((rs, rowNum) -> {
                    UUID id = UUID.fromString(rs.getString("id"));
                    long reviewCount = rs.getLong("review_count");
                    double rating = rs.getDouble("rating_sum") / reviewCount;
                    double score = rs.getDouble("score");
                    return BookDashboardDto.builder()
                            .id(id)
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .periodReviewCount(reviewCount)
                            .periodRating(rating)
                            .periodScore(score)
                            .build();
                })
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<BookDashboardDto, Dashboard> createRankingBooksItemProcessor(
            @Value("#{jobParameters['periodType']}") PeriodType periodType
    ) {
        /*
        정렬된 순서대로 랭크 부여
         */
        AtomicLong rank = new AtomicLong(1L);   // 객체 생성시 한번만 호출됨
        return bookDashboardDto ->
                // 프로세스 호출될 때마다 rank 값 1씩 증가
                Dashboard.builder()
                        .entityId(bookDashboardDto.id())
                        .rankingType(RankingType.BOOK)
                        .periodType(periodType)
                        .rank(rank.getAndIncrement())
                        .score(bookDashboardDto.periodScore())
                        .build();
    }

    @Bean
    public JpaItemWriter<Dashboard> createRankingBooksWriter() {
        // 영속성 컨텍스트에 있는 엔티티들을 데이터베이스에 저장
        return new JpaItemWriterBuilder<Dashboard>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
