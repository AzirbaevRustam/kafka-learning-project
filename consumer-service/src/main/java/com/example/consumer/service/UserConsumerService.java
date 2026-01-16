package com.example.consumer.service;

import com.example.common.dto.UserEvent;
import com.example.consumer.entity.UserEntity;
import com.example.consumer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumerService {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = "${spring.kafka.topics.user-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    @Transactional
    public void consumeUserEvent(@Payload UserEvent userEvent) {
        log.info("Received UserEvent: {}", userEvent);

        try {
            boolean userExists = userRepository.existsByEmail(userEvent.getEmail());

            if (userExists) {
                log.warn("User with email {} already exists. Skipping.", userEvent.getEmail());
                return;
            }

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userEvent.getUsername());
            userEntity.setEmail(userEvent.getEmail());
            userEntity.setFirstName(userEvent.getFirstName());
            userEntity.setLastName(userEvent.getLastName());
            userEntity.setCreatedAt(userEvent.getCreatedAt());

            UserEntity savedUser = userRepository.save(userEntity);

            log.info("User saved successfully with ID: {}", savedUser.getId());

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation for user with email {}: {}",
                    userEvent.getEmail(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing UserEvent: {}", e.getMessage(), e);
        }
    }
}