package collector.domain.stock.application;

import static collector.global.common.Const.REDIS_KEY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@ExtendWith(MockitoExtension.class)
public class StockDataProcessingServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @InjectMocks
    private StockDataProcessingService stockDataProcessingService;

    @Test
    @DisplayName("Redis에서 티커를 찾지 못했을 때 예외를 던진다")
    void should_throwException_when_noTickersFoundInRedis() {
        // Arrange
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(REDIS_KEY, 0, -1)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            stockDataProcessingService.publishStockData("short_timeframe_stock"));
    }
}