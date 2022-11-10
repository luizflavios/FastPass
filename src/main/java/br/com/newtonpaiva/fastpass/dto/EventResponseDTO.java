package br.com.newtonpaiva.fastpass.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class EventResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String ownerPhoneNumber;
    private Double ticketValue;
    private LocalDateTime dateTime;
    private String date;
    private String image;

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return getDateTime().format(formatter);
    }
}
