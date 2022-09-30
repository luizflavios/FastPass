package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import br.com.newtonpaiva.fastpass.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByTicketStatusAndPaymentMethod(TicketStatus available, PaymentMethod paymentMethod);

    Long countByTicketStatusAndPaymentMethod(TicketStatus available, PaymentMethod paymentMethod);
}
