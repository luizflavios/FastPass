package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Integer> {
    Long countByCard(Card card);

    List<Recharge> findByCardOrderByCreatedAtDesc(Card card);

    Boolean existsByCardAndValueAndCreatedAtGreaterThan(Card card, BigDecimal bigDecimal, LocalDateTime atStartOfDay);
}
