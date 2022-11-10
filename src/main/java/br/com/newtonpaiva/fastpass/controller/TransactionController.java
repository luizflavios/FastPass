package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    public static final String USER_RESPONSE_DTO = "userResponseDTO";
    @Autowired
    private FastPassUtil fastPassUtil;

    @GetMapping
    public ModelAndView loadTransactionPage() {
        ModelAndView modelAndView = new ModelAndView();
        return setRechargeNativeObjects(modelAndView);
    }

    private ModelAndView setRechargeNativeObjects(ModelAndView modelAndView) {
        modelAndView.setViewName(PageConstants.TRANSACTION_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.TRANSACTION_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.TRANSACTION_REASON);
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());
        return modelAndView;
    }
}
