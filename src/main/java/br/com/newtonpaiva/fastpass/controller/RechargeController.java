package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import br.com.newtonpaiva.fastpass.dto.RechargeResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.RechargeService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recharges")
public class RechargeController {

    public static final String USER_RESPONSE_DTO = "userResponseDTO";

    @Autowired
    private FastPassUtil fastPassUtil;
    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private RechargeService rechargeService;

    @GetMapping
    public ModelAndView loadRechargePage() {
        ModelAndView modelAndView = new ModelAndView();
        return setRechargeNativeObjects(modelAndView);
    }

    @GetMapping("/qrCode/{value}")
    public ResponseEntity<QrCodeResponseDTO> generateQrCode(@PathVariable String value) {
        return ResponseEntity.ok(rechargeService.generateQrCode(fastPassUtil.getLoggedUser(), value));
    }

    @GetMapping("/buy/{userId}/{value}")
    public ResponseEntity<String> buyTicket(@PathVariable Integer userId, @PathVariable String value) {
        return ResponseEntity.ok(rechargeService.payRecharge(value, userId));
    }

    @GetMapping("/verify-payment/{value}")
    public ResponseEntity<String> loadEventPageWithPaymentVerified(@PathVariable String value) {
        return ResponseEntity.ok(rechargeService.verifyPaymentMessage(fastPassUtil.getLoggedUser(), value));
    }

    private ModelAndView setRechargeNativeObjects(ModelAndView modelAndView) {
        modelAndView.setViewName(PageConstants.RECHARGE_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.RECHARGE_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.RECHARGE_REASON);
        modelAndView.addObject("rechargeListResponseDTO", genericMapper.toCollectionDTO(rechargeService
                .listMyRecharges(fastPassUtil.getLoggedUser()), RechargeResponseDTO.class));
        modelAndView.addObject("cardInfoResponseDTO", rechargeService.buildCardInfo(fastPassUtil.getLoggedUser()));
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());
        return modelAndView;
    }

}
