package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.EventResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.EventService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/events")
public class EventController {

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
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.EVENT_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.EVENT_REASON);
        modelAndView.addObject("userResponseDTO", fastPassUtil.getLoggedUser());
        modelAndView.addObject("eventResponseDTO", genericMapper.toCollectionDTO
                (Collections.singletonList(eventService.futureEvents(fastPassUtil.getLoggedUser())),
                        EventResponseDTO.class));
        return modelAndView;
    }

}
