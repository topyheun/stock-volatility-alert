package analyzer.domain.alert.dto;

public record AlertData(
    String text
) {

    public static AlertData of(String message, double changePercentage) {
        return new AlertData(String.format("[변동성 알림] 종목 : %s, 변동률 : %.2f%%", message, changePercentage));
    }
}
