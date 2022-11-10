package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.EventResponseDTO;
import br.com.newtonpaiva.fastpass.dto.TicketResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.EventService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/events")
public class EventController {

    public static final String USER_RESPONSE_DTO = "userResponseDTO";
    public static final String FUTURE_EVENT_RESPONSE_DTO = "futureEventResponseDTO";
    public static final String PAST_EVENT_RESPONSE_DTO = "pastEventResponseDTO";
    private static final String NEWER_EVENT_RESPONSE_DTO = "newerEventResponseDTO";
    @Autowired
    private FastPassUtil fastPassUtil;
    @Autowired
    private EventService eventService;
    @Autowired
    private GenericMapper genericMapper;

    @GetMapping
    public ModelAndView loadEventPage(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageConstants.EVENT_FILE);
        setNativePageObjects(modelAndView);
        return modelAndView;
    }

    @GetMapping("/qrCode/{id}")
    public ResponseEntity<String> generateQrCode(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.generatePaymentQrCode(id));
    }

    @GetMapping("/buy/{id}")
    public ResponseEntity<String> buyTicket(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.buyTicket(id, fastPassUtil.getLoggedUser()));
    }

    private void setNativePageObjects(ModelAndView modelAndView) {
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.EVENT_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.EVENT_REASON);
        modelAndView.addObject(USER_RESPONSE_DTO, fastPassUtil.getLoggedUser());
        modelAndView.addObject(FUTURE_EVENT_RESPONSE_DTO, genericMapper.toCollectionDTO
                (eventService.futureEvents(fastPassUtil.getLoggedUser()),
                        TicketResponseDTO.class));
        modelAndView.addObject(PAST_EVENT_RESPONSE_DTO, genericMapper.toCollectionDTO
                (eventService.pastEvents(fastPassUtil.getLoggedUser()),
                        TicketResponseDTO.class));
        modelAndView.addObject(NEWER_EVENT_RESPONSE_DTO, genericMapper.toCollectionDTO
                (eventService.newerEvents(fastPassUtil.getLoggedUser()),
                        EventResponseDTO.class));
    }

}
