package analyzer.global.config;

import analyzer.domain.stock.dto.StockData;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, StockData> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:6001, localhost:6002, localhost:6003");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "analyzer.domain.stock.dto.StockData");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "stock:analyzer.domain.stock.dto.StockData");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(StockData.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockData> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockData> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return kafkaListenerContainerFactory;
    }
}
