package com.codeit.sb06deokhugamteam2.review.infra;

import com.codeit.sb06deokhugamteam2.review.domain.ReviewBook;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewCreationCommand;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewUser;
import com.codeit.sb06deokhugamteam2.review.infra.persistence.entity.Review;
import com.codeit.sb06deokhugamteam2.review.infra.web.dto.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.infra.web.dto.ReviewDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class ReviewMapper {

    public ReviewDomain toDomain(ReviewCreateRequest request) {
        UUID bookId = request.bookId();
        UUID userId = request.userId();
        final Integer rating = request.rating();
        final String content = request.content();
        var command = new ReviewCreationCommand(bookId, userId, rating, content);
        return new ReviewDomain(command);
    }

    public Review toEntity(ReviewDomain review) {
        ReviewDomain.Snapshot snapshot = review.createSnapshot();
        return new Review().id(review.id())
                .rating(review.rating())
                .content(snapshot.content())
                .likeCount(snapshot.likes().size())
                .commentCount(snapshot.commentCount())
                .createdAt(snapshot.createdAt().atZone(ZoneId.systemDefault()).toInstant())
                .updatedAt(snapshot.updatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .deleted(snapshot.deleted());
    }

    public ReviewDto toDto(ReviewDomain review, ReviewBook book, ReviewUser user) {
        ReviewDomain.Snapshot snapshot = review.createSnapshot();
        UUID id = review.id();
        UUID bookId = book.id();
        final String bookTitle = book.title();
        final String bookThumbnailUrl = book.thumbnailUrl();
        UUID userId = user.id();
        final String userNickname = user.nickname();
        final String content = snapshot.content();
        final int rating = review.rating();
        final int likeCount = snapshot.likes().size();
        final int commentCount = snapshot.commentCount();
        final boolean likedByMe = review.isLikedBy(userId);
        LocalDateTime createdAt = snapshot.createdAt();
        LocalDateTime updatedAt = snapshot.updatedAt();
        return new ReviewDto(
                id,
                bookId,
                bookTitle,
                bookThumbnailUrl,
                userId,
                userNickname,
                content,
                rating,
                likeCount,
                commentCount,
                likedByMe,
                createdAt,
                updatedAt
        );
    }
}
