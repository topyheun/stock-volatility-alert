package collector.domain.stock.application;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import collector.domain.stock.dto.StockData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class StockDataPublishServiceTest {

    @Mock
    private KafkaTemplate<String, StockData> kafkaTemplate;

    @InjectMocks
    private StockDataPublishService stockDataPublishService;

    @Test
    @DisplayName("Stock 데이터를 수신하면 해당 데이터를 카프카 토픽으로 전송한다")
    void should_sendStockDataToKafka_when_stockDataIsProvided() {
        // Arrange
        String topic = "short_timeframe_stock";
        StockData stockData = new StockData("KRW-BTC", 85000000.0);

        // Act
        stockDataPublishService.sendStockDataToKafka(topic, stockData);

        // Assert
        verify(kafkaTemplate, times(1)).send(topic, stockData.ticker(), stockData);
    }
}