package analyzer.domain.stock.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import analyzer.domain.alert.application.AlertSlackService;
import analyzer.domain.stock.dto.StockData;
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
class StockDataAnalyzeServiceTest {

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private AlertSlackService alertSlackService;

    @InjectMocks
    private StockDataAnalyzeService stockDataAnalyzeService;

    @Test
    @DisplayName("가격 변동률이 설정 값 이상일 때 알림이 전송된다.")
    void should_SendAlert_When_PriceChangePercentExceeds() {
        // Arrange
        String ticker = "AAPL";
        double previousPrice = 100.0;
        double currentPrice = 105.0;
        double priceChangeAlertValue = 4.0;
        String redisHashKey = "test_hash";
        String slackWebhookUrl = "https://hooks.slack.com/services/test-webhook-url";
        StockData stockData = new StockData(ticker, currentPrice);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(redisHashKey, ticker)).thenReturn(previousPrice);

        ArgumentCaptor<String> tickerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> changePercentageCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        stockDataAnalyzeService.analyzeAndSendStockData(ticker, stockData, priceChangeAlertValue, redisHashKey, slackWebhookUrl);

        // Assert
        verify(alertSlackService, times(1)).sendVolatilityAlert(tickerCaptor.capture(), changePercentageCaptor.capture(), urlCaptor.capture());

        assertEquals(ticker, tickerCaptor.getValue());
        assertEquals(5.0, changePercentageCaptor.getValue());
        assertEquals(slackWebhookUrl, urlCaptor.getValue());
    }

    @Test
    @DisplayName("이전 가격이 없을 때 알림이 전송되지 않는다.")
    void should_NotSendAlert_When_NoPreviousPriceInRedis() {
        // Arrange
        String ticker = "AAPL";
        double currentPrice = 105.0;
        double priceChangeAlertValue = 4.0;
        String redisHashKey = "test_hash";
        String slackWebhookUrl = "https://hooks.slack.com/services/test-webhook-url";
        StockData stockData = new StockData(ticker, currentPrice);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(redisHashKey, ticker)).thenReturn(null);

        // Act
        stockDataAnalyzeService.analyzeAndSendStockData(ticker, stockData, priceChangeAlertValue, redisHashKey, slackWebhookUrl);

        // Assert
        verify(alertSlackService, times(0)).sendVolatilityAlert(ticker, 0.0, slackWebhookUrl);
    }

    @Test
    @DisplayName("가격 변동률이 설정 값 미만일 때 알림이 전송되지 않는다.")
    void should_NotSendAlert_When_PriceChangePercentBelow() {
        // Arrange
        String ticker = "AAPL";
        double previousPrice = 100.0;
        double currentPrice = 101.0;
        double priceChangeAlertValue = 5.0;
        String redisHashKey = "test_hash";
        String slackWebhookUrl = "https://hooks.slack.com/services/test-webhook-url";
        StockData stockData = new StockData(ticker, currentPrice);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(redisHashKey, ticker)).thenReturn(previousPrice);

        // Act
        stockDataAnalyzeService.analyzeAndSendStockData(ticker, stockData, priceChangeAlertValue, redisHashKey, slackWebhookUrl);

        // Assert
        verify(alertSlackService, times(0)).sendVolatilityAlert(ticker, 1.0, slackWebhookUrl);
    }
}