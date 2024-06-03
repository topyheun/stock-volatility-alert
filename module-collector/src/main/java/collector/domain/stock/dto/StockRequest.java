package collector.domain.stock.dto;

public record StockRequest(
    String market,
    int tradePrice
) {

}
