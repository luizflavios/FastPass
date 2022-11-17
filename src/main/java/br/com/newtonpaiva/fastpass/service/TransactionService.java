package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Transaction;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> lastTransactions(UserResponseDTO userResponseDTO) {
        Card card = cardRepository.findByUserAndActive(userResponseDTO.getId()).orElseThrow(EntityNotFoundException::new);
        return transactionRepository.findByCardOrderByCreatedAtDesc(card);
    }
}
