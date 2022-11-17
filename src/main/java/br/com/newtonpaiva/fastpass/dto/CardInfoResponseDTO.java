package br.com.newtonpaiva.fastpass.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardInfoResponseDTO {
    private Double balance;
    private Double totalInvested;
    private Integer totalProducts;
    private String totalProductsPercent;
    private Integer totalTickets;
    private String totalTicketsPercent;
    private Integer totalRecharges;
    private String totalRechargesPercent;
}
