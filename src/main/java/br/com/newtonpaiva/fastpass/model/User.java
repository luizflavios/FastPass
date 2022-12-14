package br.com.newtonpaiva.fastpass.model;

import br.com.newtonpaiva.fastpass.generic.GenericEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

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
@Table(name = "users")
public class User implements Serializable, GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    @Column(nullable = false, name = "full_name")
    private String fullName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Boolean enabled;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] userImage;
    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;
}
