package collector.domain.stock.application;

import static collector.global.common.Const.LONG_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static collector.global.common.Const.SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockDataSchedulerService {

    private final StockDataProcessingService stockDataProcessingService;

    @Scheduled(cron = "0 */5 * * * *") // 5분 주기로 수집
    public void publishStockDataShortTimeframe() {
        stockDataProcessingService.publishStockData(SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }

    @Scheduled(cron = "0 0 * * * *") // 1시간 주기로 수집
    public void publishStockDataLongTimeframe() {
        stockDataProcessingService.publishStockData(LONG_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }
}
