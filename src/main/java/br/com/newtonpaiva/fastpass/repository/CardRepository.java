package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query("select c from Card c where c.user.id = ?1 and c.active = true")
    Card findByUserAndActive(Integer id);
}
