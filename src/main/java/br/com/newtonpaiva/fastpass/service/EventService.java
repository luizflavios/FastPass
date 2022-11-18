package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import br.com.newtonpaiva.fastpass.dto.TicketResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Event;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Ticket;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.EventRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import br.com.newtonpaiva.fastpass.util.QrCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
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
    private QrCodeGenerator qrCodeGenerator;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private GenericMapper genericMapper;

    public List<Ticket> futureEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.findTop4ByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod);
    }

    public List<Ticket> pastEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.findTop4ByTicketStatusAndPaymentMethod(TicketStatus.USED, paymentMethod);
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
        return qrCodeGenerator.qrCodeEventBuilder(id, loggedUser.getId());
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

    public Page<TicketResponseDTO> pageFutureEvents(UserResponseDTO loggedUser, PageRequest pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        List<Ticket> futureTickets = ticketRepository.findByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod);
        List<TicketResponseDTO> list;

        if (futureTickets.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, futureTickets.size());
            list = futureTickets.subList(startItem, toIndex).stream()
                    .map(t -> (TicketResponseDTO) genericMapper.toDTO(t, TicketResponseDTO.class)).toList();
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), futureTickets.size());
    }

    //ALTERA STATUS DOS EVENTOS E TICKETS PARA USADO - RUN EVERY DAY AT 00:00:00HRS
    @Scheduled(cron = "0 0 00 * * *", zone = "America/Sao_Paulo")
    public void alterarStatusEventosTickets() {
        try {
            log.info(" -- INICIANDO JOB DE ALTERACAO DE STATUS DOS EVENTOS/TICKETS --");
            List<Event> events = eventRepository.findByEventStatusAndDateTimeLessThan(EventStatus.FUTURE, LocalDateTime.now());
            events.forEach(event -> event.setEventStatus(EventStatus.PAST));
            events.forEach(event -> event.getTickets().forEach(ticket -> ticket.setTicketStatus(TicketStatus.USED)));
            eventRepository.saveAllAndFlush(events);
            log.info(" -- FINALIZANDO JOB DE ALTERACAO DE STATUS DOS EVENTOS/TICKETS --");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
