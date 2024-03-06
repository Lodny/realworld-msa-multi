package com.lodny.rwfollow.repository;

import com.lodny.rwfollow.entity.Follow;
import com.lodny.rwfollow.entity.FollowId;
import org.springframework.data.repository.Repository;

public interface FollowRepository extends Repository<Follow, FollowId> {
    Follow save(Follow follow);

    void deleteById(FollowId followId);

    Follow findById(FollowId followId);
}
