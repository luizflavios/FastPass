package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.QrCodeResponseDTO;
import br.com.newtonpaiva.fastpass.dto.RechargeResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.RechargeService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.IntStream;

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
    public ModelAndView loadRechargePage(@PageableDefault(size = 3) Pageable pageable) {

        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        ModelAndView modelAndView = new ModelAndView();
        return setRechargeNativeObjects(modelAndView, currentPage, pageSize);
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

    private ModelAndView setRechargeNativeObjects(ModelAndView modelAndView, int currentPage, int pageSize) {
        modelAndView.setViewName(PageConstants.RECHARGE_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.RECHARGE_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.RECHARGE_REASON);
        modelAndView.addObject("cardInfoResponseDTO", rechargeService.buildCardInfo(fastPassUtil.getLoggedUser()));
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());

        Page<RechargeResponseDTO> rechargeResponseDTOPage = rechargeService.listMyRecharges(fastPassUtil.getLoggedUser(), PageRequest.of(currentPage, pageSize));
        modelAndView.addObject("rechargeListResponseDTO", rechargeResponseDTOPage);

        if (rechargeResponseDTOPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, rechargeResponseDTOPage.getTotalPages())
                    .boxed()
                    .toList();
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        return modelAndView;
    }

}
