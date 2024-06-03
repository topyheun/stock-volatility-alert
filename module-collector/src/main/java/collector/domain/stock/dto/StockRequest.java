package collector.domain.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockRequest(
    String market,

    @JsonProperty("trade_price")
    int tradePrice
) {

}
