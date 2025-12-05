package com.codeit.sb06deokhugamteam2.review.adapter.out;

import com.codeit.sb06deokhugamteam2.review.application.port.out.ReviewUserRepositoryPort;
import com.codeit.sb06deokhugamteam2.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ReviewUserJpaRepositoryAdapter implements ReviewUserRepositoryPort {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean existsById(UUID userId) {
        User userEntity = em.find(User.class, userId);
        return userEntity != null;
    }
}
