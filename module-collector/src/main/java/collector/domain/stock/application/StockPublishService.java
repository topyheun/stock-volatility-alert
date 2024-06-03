package collector.domain.stock.application;

import collector.domain.stock.dto.StockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPublishService {

    private final KafkaTemplate<String, StockRequest> kafkaTemplate;

    public void publish(StockRequest stockRequest) {
        kafkaTemplate.send("stock", stockRequest);
    }
}
