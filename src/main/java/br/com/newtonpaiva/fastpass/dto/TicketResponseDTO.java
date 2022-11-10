package br.com.newtonpaiva.fastpass.dto;

import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private Integer id;
    private EventResponseDTO event;
    private PaymentMethodResponseDTO paymentMethod;
    private String number;
    private TicketStatus ticketStatus;
}
