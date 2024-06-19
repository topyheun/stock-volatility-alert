package analyzer.global.common;

public final class Const {

    // Slack
    public static final String SLACK_WEBHOOK_URL_SHORT_VOLATILITY_ALERT = "https://hooks.slack.com/services/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";
    public static final String SLACK_WEBHOOK_URL_LONG_VOLATILITY_ALERT = "https://hooks.slack.com/services/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";

    // Kafka
    public static final String SHORT_STOCK_TIMEFRAME_KAFKA_TOPIC = "short_timeframe_stock";
    public static final String LONG_STOCK_TIMEFRAME_KAFKA_TOPIC = "long_timeframe_stock";

    // Redis
    public static final String SHORT_TIMEFRAME_REDIS_HASH_KEY = "short_timeframe_stock";
    public static final String LONG_TIMEFRAME_REDIS_HASH_KEY = "long_timeframe_stock";
}
