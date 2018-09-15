package com.example.ojackkyoserver;

import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Configuration
public class tempFilter {
        @Bean
        public FilterRegistrationBean getFilterRegistrationBean()
        {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CipherFilter());
            registrationBean.addUrlPatterns("*");
            return registrationBean;
        }



}
class CipherFilter implements Filter {



    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println(req.getLocalAddr());
        System.out.println(req.getRemoteAddr());
        chain.doFilter(req, res);
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void init(FilterConfig fc) throws ServletException
    {
    }
}