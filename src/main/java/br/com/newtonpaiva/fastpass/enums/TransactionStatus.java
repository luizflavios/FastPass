package br.com.newtonpaiva.fastpass.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    
    WAITING("Aguardando"),
    SUCCESSFUL("Aprovada"),
    FAILED("Falha");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }
}
