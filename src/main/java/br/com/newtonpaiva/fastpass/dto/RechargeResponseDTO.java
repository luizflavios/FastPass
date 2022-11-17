package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RechargeResponseDTO {
    
    private Integer id;
    private BigDecimal value;
    private LocalDateTime createdAt;
    private String date;

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return getCreatedAt().format(formatter);
    }
}
