package br.com.newtonpaiva.fastpass.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class QrCodeResponseDTO {
    private String qrCode;
    private BigDecimal value;
    private Integer userId;
    private Integer eventId;
}
