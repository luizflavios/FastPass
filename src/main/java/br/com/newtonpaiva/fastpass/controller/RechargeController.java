package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recharges")
public class RechargeController {

    public static final String USER_RESPONSE_DTO = "userResponseDTO";
    @Autowired
    private FastPassUtil fastPassUtil;

    @GetMapping
    public ModelAndView loadRechargePage() {
        ModelAndView modelAndView = new ModelAndView();
        return setRechargeNativeObjects(modelAndView);
    }

    private ModelAndView setRechargeNativeObjects(ModelAndView modelAndView) {
        modelAndView.setViewName(PageConstants.RECHARGE_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.RECHARGE_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.RECHARGE_REASON);
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());
        return modelAndView;
    }

}
