package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByEventStatusAndPlaceCapacityGreaterThanOrderByDateTime(EventStatus future, Integer placeCapacity);
}

