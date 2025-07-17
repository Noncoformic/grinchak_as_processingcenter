package ru.edme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate(
            @Value("${remote.retry.max-attempts}") int maxAttempts,
            @Value("${remote.retry.backoff}")    long backoff) {

        FixedBackOffPolicy backOff = new FixedBackOffPolicy();
        backOff.setBackOffPeriod(backoff);

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(maxAttempts);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(policy);
        template.setBackOffPolicy(backOff);
        return template;
    }
}
