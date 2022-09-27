package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.DashboardResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.repository.RechargeRepository;
import br.com.newtonpaiva.fastpass.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class IndexService {

    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO setDashboardPersonalInfo(UserResponseDTO loggedUser) {
        return DashboardResponseDTO.builder()
                .balanceAvailable(balanceAvailable(loggedUser))
                .futureEvents(ticketRepository.countByTicketStatus(TicketStatus.AVAILABLE))
                .pastEvents(ticketRepository.countByTicketStatus(TicketStatus.USED))
                .rechargesPerformed(rechargeRepository.count())
                .build();
    }

    private BigDecimal balanceAvailable(UserResponseDTO loggedUser) {
        return new BigDecimal("9999.99");
    }
}
