package br.com.newtonpaiva.fastpass.config;

import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.*;
import br.com.newtonpaiva.fastpass.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DevConfiguration {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private User instanceUser;
    private CreditCard instanceCreditCard;
    private PaymentMethod instancePaymentMethod;

    @PostConstruct
    private void initDevConfiguration() {
        insertUsers();
        insertCreditCard();
        insertPaymentMethods();
        insertEventsAndTickets();
    }

    private void insertUsers() {
        User user = User.builder()
                .id(1)
                .fullName("Luiz Flavio de Souza Sales Filho")
                .email("ngpbrasil@gmail.com")
                .password("admin")
                .enabled(Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .build();
        instanceUser = userRepository.saveAndFlush(user);

    }

    private void insertCreditCard() {
        CreditCard creditCard = CreditCard.builder()
                .id(1)
                .active(Boolean.TRUE)
                .cardNumber("")
                .cvv("")
                .duoDate(LocalDate.now())
                .user(instanceUser)
                .build();
        instanceCreditCard = creditCardRepository.saveAndFlush(creditCard);
    }

    private void insertPaymentMethods() {
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(1)
                .active(Boolean.TRUE)
                .creditCard(instanceCreditCard)
                .user(instanceUser)
                .createdAt(LocalDateTime.now())
                .build();
        paymentMethodRepository.saveAndFlush(paymentMethod);
    }

    private void insertEventsAndTickets() {
        Event event = Event.builder()
                .id(1)
                .eventStatus(EventStatus.FUTURE)
                .dateTime(LocalDateTime.now().plusDays(10))
                .name("Pagode Maluco")
                .description("O pagode que voce fica doidão!")
                .ownerName("Dono do Pagode Maluco")
                .ownerPhoneNumber("31 99123-4567")
                .build();

        Event event2 = Event.builder()
                .id(2)
                .eventStatus(EventStatus.FUTURE)
                .dateTime(LocalDateTime.now().plusMonths(1))
                .name("Rock Pesado")
                .description("O rock onde voce bate cabeça!")
                .ownerName("Dono do Rock Pesado")
                .ownerPhoneNumber("31 99123-4567")
                .build();

        eventRepository.saveAllAndFlush(List.of(event, event2));

        List<Event> events = eventRepository.findAll();
        events.forEach(this::createTickets);

    }

    private void createTickets(Event event) {
        int i = 1;
        while (i <= 10) {
            Ticket ticket = Ticket.builder()
                    .id(i)
                    .event(event)
                    .number(Integer.toString(i))
                    .ticketStatus(TicketStatus.AVAILABLE)
                    .build();
            ticketRepository.saveAndFlush(ticket);
            i++;
        }
    }

}
