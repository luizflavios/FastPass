package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.CardInfoResponseDTO;
import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.model.*;
import br.com.newtonpaiva.fastpass.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static br.com.newtonpaiva.fastpass.util.QrCodeGenerator.qrCodeRechargeBuilder;

@Service
@Slf4j
public class RechargeService {

    private static final String PERCENTAGE = "%";
    private static final String R$ = "R$ ";
    private static final String PARABENS_PELA_COMPRA = "<h1>PARABENS PELA SUA COMPRA</h1>";

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private RechargeRepository rechargeRepository;

    @Transactional(readOnly = true)
    public CardInfoResponseDTO buildCardInfo(UserResponseDTO loggedUser) {
        Card card = cardRepository.findByUserAndActive(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(loggedUser.getId());
        List<Transaction> transactions = transactionRepository.findByCardOrderByCreatedAtDesc(card);
        List<Recharge> recharges = rechargeRepository.findByCardOrderByCreatedAtDesc(card);
        List<Ticket> tickets = ticketRepository.findByPaymentMethod(paymentMethod);
        return customCardInfoBuilder(transactions, recharges, tickets, card);
    }

    private CardInfoResponseDTO customCardInfoBuilder(List<Transaction> transactions, List<Recharge> recharges, List<Ticket> tickets, Card card) {

        int totalCount = transactions.size();
        totalCount += recharges.size();
        totalCount += tickets.size();

        AtomicReference<Double> totalInvested = new AtomicReference<>(0.0);
        recharges.forEach(recharge -> totalInvested.updateAndGet(v -> v + recharge.getValue().doubleValue()));

        return CardInfoResponseDTO.builder()
                .totalTickets(tickets.size())
                .totalProducts(transactions.size())
                .totalRecharges(recharges.size())
                .balance(card.getBalance())
                .totalInvested(totalInvested.get())
                .totalTicketsPercent(buildStringPercentage(totalCount, tickets.size()))
                .totalProductsPercent(buildStringPercentage(totalCount, transactions.size()))
                .totalRechargesPercent(buildStringPercentage(totalCount, recharges.size()))
                .build();

    }

    private String buildStringPercentage(int total, int partial) {
        if (total != 0) {
            return BigDecimal.valueOf(partial * 100L / total) + PERCENTAGE;
        } else {
            return BigDecimal.valueOf(0) + PERCENTAGE;
        }
    }

    @Transactional(readOnly = true)
    public List<Recharge> listMyRecharges(UserResponseDTO loggedUser) {
        Card card = cardRepository.findByUserAndActive(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
        return rechargeRepository.findByCardOrderByCreatedAtDesc(card);
    }

    public QrCodeResponseDTO generateQrCode(UserResponseDTO loggedUser, String value) {
        return qrCodeRechargeBuilder(new BigDecimal(value.replace(R$, "")), loggedUser.getId());
    }

    public String payRecharge(String value, Integer userId) {
        Card card = cardRepository.findByUserAndActive(userId).orElseThrow(EntityNotFoundException::new);
        PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndActive(userId);

        Recharge recharge = Recharge.builder()
                .card(card)
                .paymentMethod(paymentMethod)
                .value(new BigDecimal(value))
                .build();
        rechargeRepository.saveAndFlush(recharge);

        card.setBalance(card.getBalance() + recharge.getValue().doubleValue());
        cardRepository.saveAndFlush(card);
        return PARABENS_PELA_COMPRA;
    }

    public String verifyPaymentMessage(UserResponseDTO loggedUser, String value) {
        Card card = cardRepository.findByUserAndActive(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
        return rechargeRepository.existsByCardAndValueAndCreatedAtGreaterThan(card, new BigDecimal(value), LocalDate.now().atStartOfDay()).toString();
    }
}
