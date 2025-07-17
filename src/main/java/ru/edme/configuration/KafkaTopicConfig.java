package ru.edme.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String TRANSACTIONS_TOPIC = "transactions";

    @Bean
    public NewTopic transactionsTopic() {
        return new NewTopic(TRANSACTIONS_TOPIC, 1, (short) 1);
        // 1 partition, replication factor = 1
    }
}
