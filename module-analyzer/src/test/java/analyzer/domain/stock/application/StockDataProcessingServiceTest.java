package analyzer.domain.stock.application;

import static analyzer.global.common.Const.LONG_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static analyzer.global.common.Const.LONG_TIMEFRAME_REDIS_HASH_KEY;
import static analyzer.global.common.Const.SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static analyzer.global.common.Const.SHORT_TIMEFRAME_REDIS_HASH_KEY;
import static analyzer.global.common.Const.SLACK_WEBHOOK_URL_LONG_VOLATILITY_ALERT;
import static analyzer.global.common.Const.SLACK_WEBHOOK_URL_SHORT_VOLATILITY_ALERT;
import static org.mockito.Mockito.verify;

import analyzer.domain.stock.dto.StockData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StockDataProcessingServiceTest {

    @Mock
    private StockDataAnalyzeService stockDataAnalyzeService;

    @InjectMocks
    private StockDataProcessingService stockDataProcessingService;

    @Test
    @DisplayName("단기 시간 프레임 토픽으로부터 레코드 수신 시 분석 서비스를 호출한다")
    void should_callAnalyzeService_when_receiveRecordFromShortTimeframeTopic() {
        // Arrange
        String key = "KRW-BTC";
        StockData stockData = new StockData("KRW-BTC", 85000000.0);
        ConsumerRecord<String, StockData> record = new ConsumerRecord<>(SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC, 0, 0, key, stockData);

        // Act
        stockDataProcessingService.processShortTimeframeStockData(record);

        // Assert
        verify(stockDataAnalyzeService).analyzeAndSendStockData(
            key,
            stockData,
            0.7,
            SHORT_TIMEFRAME_REDIS_HASH_KEY,
            SLACK_WEBHOOK_URL_SHORT_VOLATILITY_ALERT
        );
    }

    @Test
    @DisplayName("장기 시간 프레임 토픽으로부터 레코드 수신 시 분석 서비스를 호출한다")
    void should_callAnalyzeService_when_receiveRecordFromLongTimeframeTopic() {
        // Arrange
        String key = "KRW-BTC";
        StockData stockData = new StockData("KRW-BTC", 85000000.0);
        ConsumerRecord<String, StockData> record = new ConsumerRecord<>(LONG_STOCK_TIMEFRAME_KAFKA_TOPIC, 0, 0, key, stockData);

        // Act
        stockDataProcessingService.processLongTimeframeStockData(record);

        // Assert
        verify(stockDataAnalyzeService).analyzeAndSendStockData(
            key,
            stockData,
            1.0,
            LONG_TIMEFRAME_REDIS_HASH_KEY,
            SLACK_WEBHOOK_URL_LONG_VOLATILITY_ALERT
        );
    }
}