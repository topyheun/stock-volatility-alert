package collector.domain.stock.application;

import collector.domain.stock.dto.StockData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockCollectService {

    private final WebClient webClient;
    private final StockPublishService stockPublishService;

    @Scheduled(cron = "0 * * * * *")
    public void collectStock() {
        List<Stock> stocks = Arrays.asList(Stock.values());

        getStockData(stocks)
            .flatMapMany(Flux::fromArray)
            .subscribe(stockPublishService::stockDataPublish);
    }

    private Mono<StockData[]> getStockData(List<Stock> stocks) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/ticker")
                .queryParam("markets", stocks.stream()
                    .map(Stock::getTicker)
                    .collect(Collectors.joining(",")))
                .build())
            .retrieve()
            .bodyToMono(StockData[].class);
    }
}
