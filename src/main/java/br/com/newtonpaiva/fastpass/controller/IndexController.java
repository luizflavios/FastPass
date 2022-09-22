package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.enums.PageConstants;
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

    @GetMapping
    public ModelAndView loadIndexPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageConstants.INDEX_FILE);
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.INDEX_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.INDEX_REASON);
        modelAndView.addObject("userResponseDTO", fastPassUtil.getLoggedUser());
        return modelAndView;
    }

}
