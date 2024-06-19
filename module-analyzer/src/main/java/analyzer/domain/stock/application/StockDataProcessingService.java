package analyzer.domain.stock.application;

import static analyzer.global.common.Const.LONG_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static analyzer.global.common.Const.LONG_TIMEFRAME_REDIS_HASH_KEY;
import static analyzer.global.common.Const.SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC;
import static analyzer.global.common.Const.SHORT_TIMEFRAME_REDIS_HASH_KEY;
import static analyzer.global.common.Const.SLACK_WEBHOOK_URL_LONG_VOLATILITY_ALERT;
import static analyzer.global.common.Const.SLACK_WEBHOOK_URL_SHORT_VOLATILITY_ALERT;

import analyzer.domain.stock.dto.StockData;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockDataProcessingService {

    private final StockDataAnalyzeService stockDataAnalyzeService;

    private static final double SHORT_TIMEFRAME_PRICE_CHANGE_PERCENTAGE_FOR_ALERT = 0.7; // 단기 시간 프레임의 가격 변동률(%) 설정 값
    private static final double LONG_TIMEFRAME_PRICE_CHANGE_PERCENTAGE_FOR_ALERT = 1.0;  // 장기 시간 프레임의 가격 변동률(%) 설정 값

    @KafkaListener(topics = SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC, groupId = "short_stock_group")
    public void processShortTimeframeStockData(ConsumerRecord<String, StockData> record) {
        stockDataAnalyzeService.analyzeAndSendStockData(record.key(), record.value(), SHORT_TIMEFRAME_PRICE_CHANGE_PERCENTAGE_FOR_ALERT, SHORT_TIMEFRAME_REDIS_HASH_KEY, SLACK_WEBHOOK_URL_SHORT_VOLATILITY_ALERT);
    }

    @KafkaListener(topics = LONG_STOCK_TIMEFRAME_KAFKA_TOPIC, groupId = "long_stock_group")
    public void processLongTimeframeStockData(ConsumerRecord<String, StockData> record) {
        stockDataAnalyzeService.analyzeAndSendStockData(record.key(), record.value(), LONG_TIMEFRAME_PRICE_CHANGE_PERCENTAGE_FOR_ALERT, LONG_TIMEFRAME_REDIS_HASH_KEY, SLACK_WEBHOOK_URL_LONG_VOLATILITY_ALERT);
    }
}
