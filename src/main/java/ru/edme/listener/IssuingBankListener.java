package ru.edme.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.edme.configuration.KafkaTopicConfig;
import ru.edme.dto.TransactionDto;

@Service
@RequiredArgsConstructor
public class IssuingBankListener {

    @KafkaListener(
            topics = KafkaTopicConfig.TRANSACTIONS_TOPIC,
            groupId = "${KAFKA_CONSUMER_GROUP:issuing-bank-group}"
    )
    public void onTransaction(TransactionDto dto) {
        // Здесь ваша логика обработки транзакции:
        // резервирование средств, взаимодействие с банком-эмитентом и т.д.
        System.out.println("Received transaction via Kafka: " + dto);
    }
}
