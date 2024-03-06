package com.lodny.rwfollow.service;

import com.lodny.rwfollow.entity.Follow;
import com.lodny.rwfollow.entity.FollowId;
import com.lodny.rwfollow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public Follow follow(final Long followeeId, final Long followerId) {
        return followRepository.save(Follow.of(followeeId, followerId));
    }

    public void unfollow(final Long followeeId, final Long followerId) {
        followRepository.deleteById(new FollowId(followeeId, followerId));
    }

    public Boolean isFollow(final Long followeeId, final Long followerId) {
        return followRepository.findById(new FollowId(followeeId, followerId)) != null;
    }
}
