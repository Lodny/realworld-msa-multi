package com.lodny.rwuser.sys.filter;

import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwcommon.util.JwtUtil;
import com.lodny.rwuser.entity.RealWorldUser;
import com.lodny.rwuser.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter4DB extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter4DB::doFilterInternal() : 1={}", 1);

        Map<String, Object> loginInfo = JwtUtil.getAuthenticatedUser();
        if (loginInfo == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Object email = loginInfo.get("email");
        RealWorldUser foundUser = userRepository.findByEmail((String)email)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        log.info("[F] doFilterInternal() : foundUser={}", foundUser);

        loginInfo.put("user", foundUser);

        filterChain.doFilter(request, response);
    }
}
