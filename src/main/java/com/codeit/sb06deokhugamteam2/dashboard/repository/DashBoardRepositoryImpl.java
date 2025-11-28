package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.DashBoard;
import com.codeit.sb06deokhugamteam2.dashboard.entity.QDashBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class DashBoardRepositoryImpl implements DashBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<DashBoard> findPopularBookListByCursor(
            RankingType rankingType,
            PeriodType period,
            String cursor,
            Instant after,
            Sort.Direction direction,
            Integer limit
    ) {
        QDashBoard dashBoard = QDashBoard.dashBoard;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(dashBoard.rankingType.eq(rankingType));
        builder.and(dashBoard.periodType.eq(period));

        if (cursor != null && after != null) {
            if (direction == Sort.Direction.ASC) {
                builder.and(
                        dashBoard.rank.gt(Long.parseLong(cursor))
                                // 순위가 같을 경우
                                .or(dashBoard.rank.eq(Long.parseLong(cursor)).and(dashBoard.createdAt.gt(after)))
                );

            } else {
                builder.and(
                        dashBoard.rank.lt(Long.parseLong(cursor))
                                .or(dashBoard.rank.eq(Long.parseLong(cursor)).and(dashBoard.createdAt.lt(after)))
                );
            }
        }

        return queryFactory
                .selectFrom(dashBoard)
                .where(builder)
                .orderBy(direction == Sort.Direction.ASC ?
                                dashBoard.rank.asc() :      // 1등부터
                                dashBoard.rank.desc(),      // 꼴등부터
                        direction == Sort.Direction.ASC ?
                                dashBoard.createdAt.asc() :      // 오래된 순
                                dashBoard.createdAt.desc()       // 최신 순
                ).limit(limit+1L)
                .fetch();
    }
}
