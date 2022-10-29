package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private BigDecimal value;
    private Boolean active;
    private String image;
}