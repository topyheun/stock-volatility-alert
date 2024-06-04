package collector.domain.stock.application;

import collector.domain.stock.dto.StockData;
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
        getStockData()
            .flatMapMany(Flux::fromArray)
            .next()
            .subscribe(stockPublishService::stockDataPublish);
    }

    private Mono<StockData[]> getStockData() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/ticker")
                .queryParam("markets", "KRW-BTC")
                .build())
            .retrieve()
            .bodyToMono(StockData[].class);
    }
}
