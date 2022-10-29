package br.com.newtonpaiva.fastpass.model;

import br.com.newtonpaiva.fastpass.generic.GenericEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product implements Serializable, GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private Boolean active;

    @Column(columnDefinition = "text")
    private String image;
}
