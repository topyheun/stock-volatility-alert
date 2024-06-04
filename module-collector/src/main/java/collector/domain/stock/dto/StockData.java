package collector.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockData(
    @JsonProperty("market")
    String ticker,

    @JsonProperty("trade_price")
    int price
) {

}