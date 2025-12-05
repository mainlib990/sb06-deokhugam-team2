package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.dto.request.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.application.dto.response.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.request.ReviewUpdateRequest;
import com.codeit.sb06deokhugamteam2.review.application.port.in.CreateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.UpdateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.out.*;
import com.codeit.sb06deokhugamteam2.review.domain.exception.AlreadyExistsReviewException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewBookNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewUserNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewBookDomain;
import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewContentDomain;
import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewRatingDomain;
import com.codeit.sb06deokhugamteam2.review.domain.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ReviewCommandService implements CreateReviewUseCase, UpdateReviewUseCase, DeleteReviewUseCase {

    private final ReviewService service;
    private final LoadReviewUserRepositoryPort loadUserRepository;
    private final LoadReviewBookRepositoryPort loadBookRepository;
    private final SaveReviewBookRepositoryPort saveBookRepository;
    private final LoadReviewRepositoryPort loadReviewRepository;
    private final SaveReviewRepositoryPort saveReviewRepository;

    public ReviewCommandService(
            ReviewService service,
            LoadReviewUserRepositoryPort loadUserRepository,
            LoadReviewBookRepositoryPort loadBookRepository,
            SaveReviewBookRepositoryPort saveBookRepository,
            LoadReviewRepositoryPort loadReviewRepository,
            SaveReviewRepositoryPort saveReviewRepository
    ) {
        this.service = service;
        this.loadUserRepository = loadUserRepository;
        this.loadBookRepository = loadBookRepository;
        this.saveBookRepository = saveBookRepository;
        this.loadReviewRepository = loadReviewRepository;
        this.saveReviewRepository = saveReviewRepository;
    }

    @Override
    public ReviewDto createReview(ReviewCreateRequest requestBody) {
        UUID bookId = UUID.fromString(requestBody.bookId());
        UUID userId = UUID.fromString(requestBody.userId());
        var rating = new ReviewRatingDomain(requestBody.rating());
        var content = new ReviewContentDomain(requestBody.content());
        ReviewDomain review = ReviewDomain.create(bookId, userId, rating, content);

        if (!loadUserRepository.existsById(userId)) {
            throw new ReviewUserNotFoundException(userId);
        }
        if (loadReviewRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new AlreadyExistsReviewException(bookId);
        }
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ReviewBookNotFoundException(bookId));
        service.registerReview(review, book);
        saveReviewRepository.save(review.toSnapshot());
        saveBookRepository.update(book);

        return loadReviewRepository.findById(review.id(), null)
                .orElseThrow(() -> new ReviewNotFoundException(review.id()));
    }

    @Override
    public void hideReview(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);

        ReviewDomain review = loadReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.deleteReview(review, requestUserId, book);
        saveReviewRepository.softDelete(review.toSnapshot());
        saveBookRepository.update(book);
    }

    @Override
    public void deleteReview(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);

        ReviewDomain review = loadReviewRepository.findByIdWithoutDeleted(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.deleteReview(review, requestUserId, book);
        saveReviewRepository.hardDelete(review.toSnapshot());
        saveBookRepository.update(book);
    }

    @Override
    public ReviewDto updateReview(String path, String header, ReviewUpdateRequest requestBody) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);
        var newRating = new ReviewRatingDomain(requestBody.rating());
        var newContent = new ReviewContentDomain(requestBody.content());

        ReviewDomain review = loadReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.editReview(review, newRating, newContent, requestUserId, book);
        saveReviewRepository.update(review.toSnapshot());
        saveBookRepository.update(book);

        return loadReviewRepository.findById(review.id(), requestUserId)
                .orElseThrow(() -> new ReviewNotFoundException(review.id()));
    }
}
