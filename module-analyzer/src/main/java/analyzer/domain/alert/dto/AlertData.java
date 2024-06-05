package analyzer.domain.alert.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AlertData(
    String text
) {

    public static AlertData of(String message, double changePercentage) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        return new AlertData(String.format("[변동성 알림] 종목 : %s, 변동률 : %.2f%%  //  %s", message, changePercentage, formattedDateTime));
    }
}
