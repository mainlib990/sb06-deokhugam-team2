package com.codeit.sb06deokhugamteam2.like.application.service;

import com.codeit.sb06deokhugamteam2.like.application.dto.response.ReviewLikeDto;
import com.codeit.sb06deokhugamteam2.like.application.port.in.ToggleReviewLikeUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.out.*;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeDomain;
import com.codeit.sb06deokhugamteam2.like.domain.model.ReviewLikeIdDomain;
import com.codeit.sb06deokhugamteam2.like.domain.service.ReviewLikeService;
import com.codeit.sb06deokhugamteam2.like.domain.exception.ReviewLikeReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.like.domain.exception.ReviewLikeUserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ReviewLikeCommandService implements ToggleReviewLikeUseCase {

    private final ReviewLikeService service;
    private final ReviewLikeEventPublisherPort eventPublisher;
    private final LoadReviewLikeReviewRepositoryPort loadReviewRepository;
    private final LoadReviewLikeUserRepositoryPort loadUserRepository;
    private final SaveReviewLikeRepositoryPort saveReviewLikeRepository;

    public ReviewLikeCommandService(
            ReviewLikeService service,
            ReviewLikeEventPublisherPort eventPublisher,
            LoadReviewLikeReviewRepositoryPort loadReviewRepository,
            LoadReviewLikeUserRepositoryPort loadUserRepository,
            SaveReviewLikeRepositoryPort saveReviewLikeRepository
    ) {
        this.service = service;
        this.eventPublisher = eventPublisher;
        this.loadReviewRepository = loadReviewRepository;
        this.loadUserRepository = loadUserRepository;
        this.saveReviewLikeRepository = saveReviewLikeRepository;
    }

    @Override
    public ReviewLikeDto toggleReviewLike(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID userId = UUID.fromString(header);
        var reviewLikeId = new ReviewLikeIdDomain(reviewId, userId);

        if (!loadReviewRepository.existsById(reviewId)) {
            throw new ReviewLikeReviewNotFoundException(reviewId);
        }
        if (!loadUserRepository.existsById(userId)) {
            throw new ReviewLikeUserNotFoundException(userId);
        }
        ReviewLikeDomain reviewLike = saveReviewLikeRepository.findById(reviewLikeId)
                .map(ReviewLikeDomain::from)
                .orElseGet(() -> ReviewLikeDomain.create(reviewId, userId));
        service.toggleLike(reviewLike);
        reviewLike.getEvents().forEach(eventPublisher::publish);
        reviewLike.clearEvents();
        if (reviewLike.isLiked()) {
            saveReviewLikeRepository.save(reviewLike.toSnapshot());
        } else {
            saveReviewLikeRepository.delete(reviewLike.toSnapshot());
        }

        return new ReviewLikeDto(reviewLike.reviewId(), reviewLike.userId(), reviewLike.isLiked());
    }
}
