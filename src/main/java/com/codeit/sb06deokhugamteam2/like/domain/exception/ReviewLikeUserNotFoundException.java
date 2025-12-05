package com.codeit.sb06deokhugamteam2.like.domain.exception;

import java.util.UUID;

public class ReviewLikeUserNotFoundException extends ReviewLikeException {

    public ReviewLikeUserNotFoundException(UUID requestUserId) {
        super("User not found: '%s'".formatted(requestUserId));
    }
}
