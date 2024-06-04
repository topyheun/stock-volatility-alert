package collector.domain.stock.application;

import collector.domain.stock.dto.StockData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPublishService {

    private final KafkaTemplate<String, StockData> kafkaTemplate;

    public void stockDataPublish(StockData stockData) {
        kafkaTemplate.send("stock", stockData.ticker(), stockData);
    }
}
