package br.com.newtonpaiva.fastpass.model;

import br.com.newtonpaiva.fastpass.enums.EmailStatus;
import br.com.newtonpaiva.fastpass.enums.EmailType;
import br.com.newtonpaiva.fastpass.generic.GenericEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "emails")
public class Email implements Serializable, GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User to;

    @Enumerated
    @Column(name = "email_type", nullable = false)
    private EmailType type;

    @Enumerated
    @Column(name = "email_status", nullable = false)
    private EmailStatus status;

    @CreationTimestamp
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Transient
    private String text;

    @Transient
    private String subject;
}
