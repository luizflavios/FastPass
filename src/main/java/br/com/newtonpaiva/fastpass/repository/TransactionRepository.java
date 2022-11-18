package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByCardOrderByCreatedAtDesc(Card card);

    List<Transaction> findTop4ByCardOrderByCreatedAtDesc(Card card);
}
