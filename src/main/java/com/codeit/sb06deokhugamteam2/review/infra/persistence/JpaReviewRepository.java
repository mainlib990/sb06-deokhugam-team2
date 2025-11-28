package com.codeit.sb06deokhugamteam2.review.infra.persistence;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewRepository;
import com.codeit.sb06deokhugamteam2.review.infra.ReviewMapper;
import com.codeit.sb06deokhugamteam2.review.infra.persistence.entity.Review;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public class JpaReviewRepository implements ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    private final ReviewMapper reviewMapper;

    public JpaReviewRepository(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Optional<UUID> findByBookIdAndUserId(
            UUID bookId,
            UUID userId
    ) {
        Review review = em.createQuery("""
                        SELECT r
                        FROM Review r
                        JOIN FETCH r.book b
                        WHERE b.id = :bookId AND r.user.id = :userId
                        """, Review.class)
                .setParameter("bookId", bookId)
                .setParameter("userId", userId)
                .getSingleResult();

        if (review == null) {
            return Optional.empty();
        }
        return Optional.of(review.book().getId());
    }

    @Override
    @Transactional
    public void save(ReviewDomain review) {
        Book book = em.getReference(Book.class, review.bookId());
        User user = em.getReference(User.class, review.userId());
        Review newReview = reviewMapper.toEntity(review).book(book).user(user);
        em.persist(newReview);
    }
}
