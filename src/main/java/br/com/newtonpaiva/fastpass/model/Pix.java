package br.com.newtonpaiva.fastpass.model;

import br.com.newtonpaiva.fastpass.generic.GenericEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pix")
public class Pix implements Serializable, GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, name = "qr_code")
    private String qrCode;

}
