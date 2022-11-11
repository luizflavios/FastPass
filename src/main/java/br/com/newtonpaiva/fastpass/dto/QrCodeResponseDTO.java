package br.com.newtonpaiva.fastpass.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class QrCodeResponseDTO {
    private String qrCode;
    private Integer eventId;
}
