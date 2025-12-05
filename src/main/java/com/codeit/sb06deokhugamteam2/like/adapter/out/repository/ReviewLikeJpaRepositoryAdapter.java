package com.codeit.sb06deokhugamteam2.like.adapter.out.repository;

import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLike;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLikeId;
import com.codeit.sb06deokhugamteam2.like.adapter.out.mapper.ReviewLikeMapper;
import com.codeit.sb06deokhugamteam2.like.application.port.out.SaveReviewLikeRepositoryPort;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeDomain;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeIdDomain;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReviewLikeJpaRepositoryAdapter implements SaveReviewLikeRepositoryPort {

    private final ReviewLikeMapper reviewLikeMapper;

    public ReviewLikeJpaRepositoryAdapter(ReviewLikeMapper reviewLikeMapper) {
        this.reviewLikeMapper = reviewLikeMapper;
    }

    @PersistenceContext
    private EntityManager em;

    private <T> JPAQuery<T> select(Expression<T> expr) {
        return new JPAQuery<>(em).select(expr);
    }

    @Override
    public Optional<ReviewLikeDomain.Snapshot> findById(ReviewLikeIdDomain reviewLikeId) {
        ReviewLikeId reviewLikeIdEntity = reviewLikeMapper.toEntityId(reviewLikeId);
        ReviewLike reviewLikeEntity = em.find(ReviewLike.class, reviewLikeIdEntity);
        return Optional.ofNullable(reviewLikeEntity)
                .map(reviewLikeMapper::toDomain);
    }

    @Override
    public void save(ReviewLikeDomain.Snapshot reviewLikeSnapshot) {
        ReviewLike reviewLikeEntity = reviewLikeMapper.toEntity(reviewLikeSnapshot);
        em.persist(reviewLikeEntity);
    }

    @Override
    public void delete(ReviewLikeDomain.Snapshot reviewLikeSnapshot) {
        var reviewLikeIdEntity = new ReviewLikeId()
                .reviewId(reviewLikeSnapshot.reviewId())
                .userId(reviewLikeSnapshot.userId());
        ReviewLike reviewLikeEntity = em.getReference(ReviewLike.class, reviewLikeIdEntity);
        em.remove(reviewLikeEntity);
    }
}
