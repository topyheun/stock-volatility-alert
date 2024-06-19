package collector.domain.stock.application;

import collector.domain.stock.dto.StockData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockDataFetchService {

    private final WebClient webClient;

    public Mono<StockData[]> fetchStockData(List<String> tickers) {
        String marketParams = String.join(",", tickers);

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/ticker")
                .queryParam("markets", marketParams)
                .build())
            .retrieve()
            .bodyToMono(StockData[].class);
    }
}
