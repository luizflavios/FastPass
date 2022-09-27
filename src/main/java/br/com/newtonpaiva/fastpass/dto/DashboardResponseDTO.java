package br.com.newtonpaiva.fastpass.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DashboardResponseDTO {
    private BigDecimal balanceAvailable;
    private Long pastEvents;
    private Long futureEvents;
    private Long rechargesPerformed;
}
