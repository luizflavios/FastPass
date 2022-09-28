package br.com.newtonpaiva.fastpass.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardResponseDTO {
    private Double balanceAvailable;
    private Long pastEvents;
    private Long futureEvents;
    private Long rechargesPerformed;
}
