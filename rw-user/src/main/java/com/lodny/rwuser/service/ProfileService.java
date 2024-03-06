package com.lodny.rwuser.service;

import com.lodny.rwuser.entity.RealWorldUser;
import com.lodny.rwuser.entity.dto.ProfileResponse;
import com.lodny.rwuser.entity.dto.UserResponse;
import com.lodny.rwuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(final String username, final UserResponse loginUser) {
        RealWorldUser foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        log.info("getProfile() : foundUser={}", foundUser);

        if (loginUser == null)
            return ProfileResponse.of(foundUser, false);

        //todo::WebClient
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + loginUser.token());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:8081/api/profiles/" + username + "/follow", HttpMethod.GET, entity, Boolean.class);
        Boolean following = response.getBody();
        log.info("getProfile() : following={}", following);

        return ProfileResponse.of(foundUser, following);
    }
}
