package collector.domain.stock.application;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import collector.domain.stock.dto.StockData;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class StockDataFetchServiceTest {

    @Test
    @DisplayName("주어진 Ticker 리스트에 대한 주식 데이터 가져오기 요청이 성공하면 해당 데이터를 반환한다")
    void should_fetchStockData_when_requestIsSuccess() throws IOException {
        // Arrange
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        StockDataFetchService sut = new StockDataFetchService(webClient);

        List<String> tickers = Arrays.asList("KRW-BTC", "KRW-ETH");
        String responseBody = "[{\"market\":\"KRW-BTC\",\"trade_price\":80000000.0},{\"market\":\"KRW-ETH\",\"trade_price\":4500000.0}]";
        mockWebServer.enqueue(new MockResponse()
            .setBody(responseBody)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200));

        // Act
        StockData[] actual = sut.fetchStockData(tickers).block();

        // Assert
        StockData[] expected = {
            new StockData("KRW-BTC", 80000000.0),
            new StockData("KRW-ETH", 4500000.0)
        };
        assertArrayEquals(expected, actual);
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("주식 데이터 가져오기 요청이 실패하면 Exception을 발생시킨다")
    void should_throwException_when_requestIsFail() throws IOException {
        // Arrange
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        StockDataFetchService sut = new StockDataFetchService(webClient);

        List<String> tickers = Arrays.asList("KRW-BTC", "KRW-ETH");
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        // Act & Assert
        assertThrows(WebClientResponseException.class, () -> {
            sut.fetchStockData(tickers).block();
        });
        mockWebServer.shutdown();
    }
}