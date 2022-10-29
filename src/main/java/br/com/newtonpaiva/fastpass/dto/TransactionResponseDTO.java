package br.com.newtonpaiva.fastpass.dto;

import br.com.newtonpaiva.fastpass.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TransactionResponseDTO {

    private Integer id;
    private EventResponseDTO event;
    private ProductResponseDTO product;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private String date;

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return getCreatedAt().format(formatter);
    }

}
