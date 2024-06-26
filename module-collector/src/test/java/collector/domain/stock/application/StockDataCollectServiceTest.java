package collector.domain.stock.application;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import collector.domain.stock.dto.StockData;
import collector.global.common.Const;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class StockDataCollectServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private StockDataCollectService sut;

    @Captor
    private ArgumentCaptor<List<String>> captor;

    @Test
    @DisplayName("주어진 티커 리스트로 주식 데이터를 성공적으로 가져오면 해당 데이터를 Redis에 저장한다")
    void should_saveStockDataInRedis_when_fetchAndSaveStockDataIsSuccessful() {
        // Arrange
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        StockData[] stockDataArray = new StockData[]{
            new StockData("KRW-BTC", 80000000.0),
            new StockData("KRW-ETH", 4500000.0)
        };

        // Mock WebClient calls
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/market/all")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(StockData[].class)).thenReturn(Mono.just(stockDataArray));

        // Act
        sut.fetchAndSaveStockData();

        // Assert
        verify(listOperations).rightPushAll(eq(Const.REDIS_KEY), captor.capture());
        List<String> capturedTickers = captor.getValue();
        List<String> expectedTickers = List.of("KRW-BTC", "KRW-ETH");
        assertArrayEquals(expectedTickers.toArray(), capturedTickers.toArray());
    }
}