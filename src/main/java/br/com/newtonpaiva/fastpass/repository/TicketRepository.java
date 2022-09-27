package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.enums.TicketStatus;
import br.com.newtonpaiva.fastpass.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Long countByTicketStatus(TicketStatus available);
}
