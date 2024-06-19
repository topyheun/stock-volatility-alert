package collector.domain.stock.application;

import static collector.global.common.Const.LONG_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static collector.global.common.Const.REDIS_KEY;
import static collector.global.common.Const.SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StockDataPublishSchedulerService {

    private final RedisTemplate<String, String> redisTemplate;
    private final StockDataFetchService stockDataFetchService;
    private final StockDataPublishService stockDataPublishService;

    private static final int CHUNK_SIZE = 50;

    @Scheduled(cron = "0 */5 * * * *") // 5분 주기로 수집
    public void publishStockDataShortTimeframe() {
        publishStockData(SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }

    @Scheduled(cron = "0 0 * * * *") // 1시간 주기로 수집
    public void publishStockDataLongTimeframe() {
        publishStockData(LONG_STOCK_TIMEFRAME_KAFKA_TOPIC);
    }

    private void publishStockData(String topic) {
        List<String> tickers = redisTemplate.opsForList().range(REDIS_KEY, 0, -1);
        if (tickers == null || tickers.isEmpty()) {
            throw new IllegalStateException("No tickers found in Redis");
        }

        Flux.fromIterable(tickers)
            .buffer(CHUNK_SIZE)
            .flatMap(stockDataFetchService::fetchStockData)
            .flatMap(Flux::fromArray)
            .subscribe(stockData -> stockDataPublishService.sendStockDataToKafka(topic, stockData));
    }
}
