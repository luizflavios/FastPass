package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Event;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Ticket;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.EventRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static br.com.newtonpaiva.fastpass.util.QrCodeGenerator.qrCodeEventBuilder;

@Service
public class EventService {

    private static final String PARABENS_PELA_COMPRA = "<h1>PARABENS PELA SUA COMPRA</h1>";

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<Ticket> futureEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.findByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod);
    }

    public List<Ticket> pastEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.findByTicketStatusAndPaymentMethod(TicketStatus.USED, paymentMethod);
    }

    public List<Event> newerEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return buildNewerEvents(paymentMethod);
    }

    private List<Event> buildNewerEvents(PaymentMethod paymentMethod) {
        List<Event> myFutureEvents = new ArrayList<>();

        ticketRepository.findByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod)
                .forEach(ticket -> myFutureEvents.add(ticket.getEvent()));

        return eventRepository.findByEventStatusAndPlaceCapacityGreaterThanOrderByDateTime(EventStatus.FUTURE, 0).stream()
                .filter(element -> !myFutureEvents.contains(element))
                .toList();
    }

    public QrCodeResponseDTO generatePaymentQrCode(Integer id, UserResponseDTO loggedUser) {
        return qrCodeEventBuilder(id, loggedUser.getId());
    }

    public String buyTicket(Integer id, Integer userId) {

        Event event = eventRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(userId);
        Card card = recoverCardByPaymentMethod(paymentMethod);

        String itsClear = checkPaymentRequiriments(event, card);
        if (!itsClear.equalsIgnoreCase("")) {
            return itsClear;
        }

        event.setPlaceCapacity(event.getPlaceCapacity() - 1);

        Ticket ticket = Ticket.builder()
                .event(event)
                .paymentMethod(paymentMethod)
                .ticketStatus(TicketStatus.AVAILABLE)
                .build();
        ticketRepository.saveAndFlush(ticket);

        eventRepository.saveAndFlush(event);

        card.setBalance(card.getBalance() - Double.parseDouble(decfor.format(event.getTicketValue())));
        cardRepository.saveAndFlush(card);
        return PARABENS_PELA_COMPRA;
    }

    private Card recoverCardByPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethod.getUser().getCards().stream().filter(card ->
                card.getActive().equals(Boolean.TRUE)).findFirst().orElseThrow(EntityNotFoundException::new);
    }

    private String checkPaymentRequiriments(Event event, Card card) {

        String error = "";

        if (event.getPlaceCapacity() == 0) {
            error = "<h1>Não existem ingressos mais disponíveis para a compra!</h1>";
        }

        if (card.getBalance() < event.getTicketValue()) {
            error = "<h1>Saldo insuficiente para compra!</h1>";
        }

        return error;
    }

    public String verifyPaymentMessage(UserResponseDTO loggedUser, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.existsByEventAndPaymentMethod(event, paymentMethod).toString();
    }
}
