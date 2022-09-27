package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Integer> {
}
