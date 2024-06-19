package collector.domain.stock.application;

import collector.domain.stock.dto.StockData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockDataPublishService {

    private final KafkaTemplate<String, StockData> kafkaTemplate;

    public void sendStockDataToKafka(String topic, StockData stockData) {
        kafkaTemplate.send(topic, stockData.ticker(), stockData);
    }
}
