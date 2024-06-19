package analyzer.domain.stock.application;

import analyzer.domain.alert.application.AlertSlackService;
import analyzer.domain.stock.dto.StockData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StockDataAnalyzeService {

    private final RedisTemplate<String, Double> redisTemplate;
    private final AlertSlackService alertSlackService;

    public void analyzeAndSendStockData(String ticker, StockData stockData, double priceChangePercentageForAlert, String redisHashKey, String slackWebhookUrl) {
        double currentPrice = stockData.price();

        Double previousPrice = (Double) redisTemplate.opsForHash().get(redisHashKey, ticker);
        if (previousPrice != null) {
            double priceChangePercentage = calculatePriceChangePercentage(currentPrice, previousPrice);
            if (Math.abs(priceChangePercentage) >= priceChangePercentageForAlert) {
                alertSlackService.sendVolatilityAlert(ticker, priceChangePercentage, slackWebhookUrl);
            }
        }
        redisTemplate.opsForHash().put(redisHashKey, ticker, currentPrice);
    }

    private double calculatePriceChangePercentage(double currentPrice, double previousPrice) {
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }
}
