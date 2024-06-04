package analyzer.domain.stock.application;

import analyzer.domain.alert.application.AlertSlackService;
import analyzer.domain.stock.dto.StockData;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockAnalyzeService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final AlertSlackService alertSlackService;

    private static final String REDIS_STOCK_HASH = "stock_hash";

    @KafkaListener(topics = "stock", groupId = "stock_group")
    public void analyzeStockData(ConsumerRecord<String, StockData> record) {
        String ticker = record.key();
        int currentPrice = record.value().price();

        Integer previousPrice = (Integer) redisTemplate.opsForHash().get(REDIS_STOCK_HASH, ticker);
        if (previousPrice != null) {
            double changePercentage = getChangePercentage(currentPrice, previousPrice);
            if (Math.abs(changePercentage) >= 0.01) {
                alertSlackService.sendVolatilityAlert(ticker, changePercentage);
            }
        }
        redisTemplate.opsForHash().put(REDIS_STOCK_HASH, ticker, currentPrice);
    }

    private double getChangePercentage(int currentPrice, int previousPrice) {
        return ((double) (currentPrice - previousPrice) / previousPrice) * 100;
    }
}
