package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.Event;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Ticket;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.EventRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<Event> futureEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        List<Ticket> tickets = ticketRepository.findByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod);
        List<Event> events = new ArrayList<>();
        tickets.forEach(ticket -> events.add(ticket.getEvent()));
        return events;
    }
}
