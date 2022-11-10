package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.Event;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Ticket;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.EventRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class EventService {

    public static final String DATA_FILE_JPEG_BASE_64 = "data:@file/jpeg;base64,";
    private static final String PARABENS_PELA_COMPRA = "<h1>PARABENS PELA SUA COMPRA</h1>";

    @Value("${api.base.url}")
    private String apiBaseUrl;

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

    public String generatePaymentQrCode(Integer id) {
        int imageSize = 400;
        try {
            String url = String.format("%s/events/buy/%s", apiBaseUrl, id.toString());
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,
                    imageSize, imageSize);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "jpeg", bos);
            return DATA_FILE_JPEG_BASE_64 + Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String buyTicket(Integer id, UserResponseDTO loggedUser) {
        Event event = eventRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        event.setPlaceCapacity(event.getPlaceCapacity() - 1);
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        Ticket ticket = Ticket.builder()
                .event(event)
                .paymentMethod(paymentMethod)
                .ticketStatus(TicketStatus.AVAILABLE)
                .build();
        ticketRepository.saveAndFlush(ticket);
        eventRepository.saveAndFlush(event);
        return PARABENS_PELA_COMPRA;
    }
}
