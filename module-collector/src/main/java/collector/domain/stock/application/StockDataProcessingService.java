package collector.domain.stock.application;

import static collector.global.common.Const.REDIS_KEY;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StockDataProcessingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final StockDataFetchService stockDataFetchService;
    private final StockDataPublishService stockDataPublishService;

    private static final int CHUNK_SIZE = 50;

    public void publishStockData(String topic) {
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
