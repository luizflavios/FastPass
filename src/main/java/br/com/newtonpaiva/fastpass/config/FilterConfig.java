package br.com.newtonpaiva.fastpass.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.List;

@Component
public class FilterConfig extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (Boolean.TRUE.equals(containsOpenUrl(request.getRequestURI()))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (request.getSession().getAttribute("user") != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(new HttpServletRequestWrapper(request) {
                    @Override
                    public String getRequestURI() {
                        return "/";
                    }
                }, servletResponse);
            }
        }
    }

    private Boolean containsOpenUrl(String uri) {
        List<String> openUrls = List.of("/", "/login", "/logout", "/sign-up", "/forgot-password", "/active-account", "/users/email-verified/");
        return openUrls.stream().anyMatch(uri::contains) || uri.contains("assets");
    }

}
