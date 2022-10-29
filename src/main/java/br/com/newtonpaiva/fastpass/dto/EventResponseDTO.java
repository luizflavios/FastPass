package br.com.newtonpaiva.fastpass.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class EventResponseDTO {
    
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String date;

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return getDateTime().format(formatter);
    }
}
