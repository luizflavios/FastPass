package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    @Query("select p from PaymentMethod p where p.user.id = ?1 and p.active = true")
    PaymentMethod findByUserAndActive(Integer id);
}
