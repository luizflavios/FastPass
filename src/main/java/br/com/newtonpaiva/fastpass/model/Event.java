package br.com.newtonpaiva.fastpass.model;

import br.com.newtonpaiva.fastpass.enums.EventStatus;
import br.com.newtonpaiva.fastpass.generic.GenericEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event implements Serializable, GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "owner_name")
    private String ownerName;

    @Column(nullable = false, name = "owner_phone_number")
    private String ownerPhoneNumber;

    @Column(nullable = false, name = "date_time")
    private LocalDateTime dateTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "event")
    private List<Ticket> tickets;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "event_status", nullable = false)
    private EventStatus eventStatus;
}
