package com.lodny.rwuser.repository;


import com.lodny.rwuser.entity.RealWorldUser;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<RealWorldUser, Long> {
    RealWorldUser save(final RealWorldUser user);
    Optional<RealWorldUser> findByEmail(final String email);
    Optional<RealWorldUser> findByUsername(final String username);
}
