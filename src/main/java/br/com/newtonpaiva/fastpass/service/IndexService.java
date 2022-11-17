package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.DashboardResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.PaymentMethodRepository;
import br.com.newtonpaiva.fastpass.repository.RechargeRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class IndexService {

    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO setDashboardPersonalInfo(UserResponseDTO loggedUser) {
        return DashboardResponseDTO.builder()
                .balanceAvailable(balanceAvailable(loggedUser))
                .futureEvents(futureEvents(loggedUser))
                .pastEvents(pastEvents(loggedUser))
                .rechargesPerformed(rechargesPerformed(loggedUser))
                .build();
    }

    private Double balanceAvailable(UserResponseDTO loggedUser) {
        Card card = cardRepository.findByUserAndActive(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
        return card.getBalance();
    }

    private Long futureEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.countByTicketStatusAndPaymentMethod(TicketStatus.AVAILABLE, paymentMethod);
    }

    private Long pastEvents(UserResponseDTO loggedUser) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        return ticketRepository.countByTicketStatusAndPaymentMethod(TicketStatus.USED, paymentMethod);
    }

    public Long rechargesPerformed(UserResponseDTO loggedUser) {
        Card card = cardRepository.findByUserAndActive(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
        return rechargeRepository.countByCard(card);
    }

}
