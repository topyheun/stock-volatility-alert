package analyzer.domain.stock.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import analyzer.domain.alert.application.AlertSlackService;
import analyzer.domain.stock.dto.StockData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
class StockAnalyzeServiceTest {

    @Mock
    private RedisTemplate<String, Integer> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private AlertSlackService alertSlackService;

    @InjectMocks
    private StockAnalyzeService stockAnalyzeService;

    @Test
    @DisplayName("가격 변동률이 0.01% 이상일 때 알림이 전송된다.")
    void should_SendAlert_When_PriceChangeGreaterThanOnePercent() {
        // Arrange
        String ticker = "AAPL";
        int previousPrice = 100;
        int currentPrice = 101;
        ConsumerRecord<String, StockData> record = new ConsumerRecord<>("stock", 0, 0, ticker, new StockData(ticker, currentPrice));

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("stock_hash", ticker)).thenReturn(previousPrice);

        ArgumentCaptor<String> tickerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> changePercentageCaptor = ArgumentCaptor.forClass(Double.class);

        // Act
        stockAnalyzeService.analyzeStockData(record);

        // Assert
        verify(alertSlackService, times(1)).sendVolatilityAlert(tickerCaptor.capture(), changePercentageCaptor.capture());

        assertEquals(ticker, tickerCaptor.getValue());
        assertEquals(1.0, changePercentageCaptor.getValue());
    }
}