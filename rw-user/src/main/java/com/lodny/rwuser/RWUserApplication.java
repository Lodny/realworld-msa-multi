package com.lodny.rwuser;

import com.lodny.rwcommon.filter.JwtFilter;
import com.lodny.rwcommon.interceptor.JwtTokenInterceptor;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwcommon.resolver.LoginUserMethodArgumentResolver;
import com.lodny.rwcommon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperty.class)
@SpringBootApplication
@RequiredArgsConstructor
public class RWUserApplication {
	private final JwtProperty jwtProperty;

	public static void main(String[] args) {
		SpringApplication.run(RWUserApplication.class, args);
	}

	@Bean
	public JwtUtil getJwtUtil() {
		return new JwtUtil(jwtProperty);
	}

	@Bean
	public JwtFilter getJwtFilter() {
		return new JwtFilter(jwtProperty, getJwtUtil());
	}

	@Bean
	public JwtTokenInterceptor getJwtTokenInterceptor() {
		return new JwtTokenInterceptor();
	}

	@Bean
	public LoginUserMethodArgumentResolver getLoginUserMethodArgumentResolver() {
		return new LoginUserMethodArgumentResolver();
	}
}
