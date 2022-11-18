package br.com.newtonpaiva.fastpass.service;

import br.com.newtonpaiva.fastpass.dto.TransactionResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.Card;
import br.com.newtonpaiva.fastpass.model.Transaction;
import br.com.newtonpaiva.fastpass.repository.CardRepository;
import br.com.newtonpaiva.fastpass.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> lastTransactions(UserResponseDTO userResponseDTO) {
        Card card = cardRepository.findByUserAndActive(userResponseDTO.getId()).orElseThrow(EntityNotFoundException::new);
        return transactionRepository.findTop4ByCardOrderByCreatedAtDesc(card);
    }

    public Page<TransactionResponseDTO> pageLastTransactions(UserResponseDTO userResponseDTO, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Card card = cardRepository.findByUserAndActive(userResponseDTO.getId()).orElseThrow(EntityNotFoundException::new);
        List<Transaction> transactions = transactionRepository.findByCardOrderByCreatedAtDesc(card);
        List<TransactionResponseDTO> list;

        if (transactions.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transactions.size());
            list = transactions.subList(startItem, toIndex).stream()
                    .map(t -> (TransactionResponseDTO) genericMapper.toDTO(t, TransactionResponseDTO.class)).toList();
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), transactions.size());
    }
}
