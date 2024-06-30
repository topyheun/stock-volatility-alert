package collector.domain.stock.application;

import static collector.global.common.Const.LONG_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static collector.global.common.Const.SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StockDataSchedulerServiceTest {

    @Mock
    private StockDataProcessingService stockDataProcessingService;

    @InjectMocks
    private StockDataSchedulerService stockDataSchedulerService;

    @Test
    @DisplayName("스케줄러 동작 시 짧은 기간 주식 데이터를 처리 서비스를 호출한다")
    void should_callPublishStockDataShortTimeframe_when_schedulerRuns() {
        // Act
        stockDataSchedulerService.publishStockDataShortTimeframe();

        // Assert
        verify(stockDataProcessingService).publishStockData(SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }

    @Test
    @DisplayName("스케줄러 동작 시 긴 기간 주식 데이터를 처리 서비스를 호출한다")
    void should_callPublishStockDataLongTimeframe_when_schedulerRuns() {
        // Act
        stockDataSchedulerService.publishStockDataLongTimeframe();

        // Assert
        verify(stockDataProcessingService).publishStockData(LONG_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }
}