package com.codeit.sb06deokhugamteam2.review.infra.persistence;

import com.codeit.sb06deokhugamteam2.review.domain.ReviewUser;
import com.codeit.sb06deokhugamteam2.review.domain.UserRepository;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<ReviewUser> findById(UUID userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return Optional.empty();
        }
        var reviewUser = new ReviewUser(user.getId(), user.getNickname());
        return Optional.of(reviewUser);
    }
}
