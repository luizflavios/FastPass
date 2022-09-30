package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
