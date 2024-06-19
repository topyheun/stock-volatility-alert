package collector.domain.stock.application;

import static collector.global.common.Const.REDIS_KEY;

import collector.domain.stock.dto.StockData;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StockDataCollectService {

    private final WebClient webClient;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        fetchAndSaveStockData();
    }

    public void fetchAndSaveStockData() {
        webClient.get()
            .uri("/market/all")
            .retrieve()
            .bodyToMono(StockData[].class)
            .doOnNext(this::saveStockDataInRedis)
            .subscribe();
    }

    private void saveStockDataInRedis(StockData[] stockData) {
        List<String> tickers = Arrays.stream(stockData)
            .map(StockData::ticker)
            .filter(ticker -> ticker.startsWith("KRW"))
            .collect(Collectors.toList());

        redisTemplate.opsForList().rightPushAll(REDIS_KEY, tickers);
    }
}
