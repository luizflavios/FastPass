package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.TransactionResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.TransactionService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    public static final String USER_RESPONSE_DTO = "userResponseDTO";

    @Autowired
    private FastPassUtil fastPassUtil;
    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ModelAndView loadTransactionPage(@PageableDefault(size = 5) Pageable pageable) {

        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        ModelAndView modelAndView = new ModelAndView();
        return setTransactionNativeObjects(modelAndView, currentPage, pageSize);
    }

    private ModelAndView setTransactionNativeObjects(ModelAndView modelAndView, int currentPage, int pageSize) {
        modelAndView.setViewName(PageConstants.TRANSACTION_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.TRANSACTION_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.TRANSACTION_REASON);
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());

        Page<TransactionResponseDTO> transactionResponseDTOPage = transactionService.pageLastTransactions(fastPassUtil.getLoggedUser(), PageRequest.of(currentPage, pageSize));
        modelAndView.addObject("transactionPageResponseDTO", transactionResponseDTOPage);

        if (transactionResponseDTOPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, transactionResponseDTOPage.getTotalPages())
                    .boxed()
                    .toList();
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        return modelAndView;
    }
}
