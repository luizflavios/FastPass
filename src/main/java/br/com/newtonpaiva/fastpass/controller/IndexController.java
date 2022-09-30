package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.EventResponseDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.service.EventService;
import br.com.newtonpaiva.fastpass.service.IndexService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private FastPassUtil fastPassUtil;
    @Autowired
    private IndexService indexService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GenericMapper genericMapper;

    @GetMapping
    public ModelAndView loadIndexPage() {
        ModelAndView modelAndView = new ModelAndView();
        setIndexObjects(modelAndView, fastPassUtil.getLoggedUser());
        return modelAndView;
    }

    public void setIndexObjects(ModelAndView modelAndView, UserResponseDTO userResponseDTO) {
        modelAndView.setViewName(PageConstants.INDEX_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.INDEX_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.INDEX_REASON);
        modelAndView.addObject("userResponseDTO", userResponseDTO);
        modelAndView.addObject("eventResponseDTO", genericMapper.toCollectionDTO
                (eventService.futureEvents(userResponseDTO),
                        EventResponseDTO.class));
        modelAndView.addObject("dashboardResponseDTO", indexService.setDashboardPersonalInfo(userResponseDTO));
    }
}
