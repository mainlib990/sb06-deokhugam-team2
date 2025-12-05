package com.codeit.sb06deokhugamteam2.like.adapter.out.event;

import com.codeit.sb06deokhugamteam2.like.application.port.out.ReviewLikeEventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReviewLikeSpringEventPublisherAdapter implements ReviewLikeEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public ReviewLikeSpringEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
