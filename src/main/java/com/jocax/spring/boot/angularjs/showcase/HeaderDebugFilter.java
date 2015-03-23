package com.jocax.spring.boot.angularjs.showcase;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

@Component
public class HeaderDebugFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderDebugFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (doLog(request)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            LOG.info("Before request. Method: {}, Path: {}, header(s): {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI() + httpServletRequest.getQueryString() , getRequestHeaders(request));
        }

        chain.doFilter(request, response);

        if (doLog(request)) {
            LOG.info("After response header(s): {}", getResponseHeaders(response));
        }
    }

    private String getRequestHeaders(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Enumeration<String> headers = httpServletRequest.getHeaderNames();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\r").append("Path: ").append(((HttpServletRequest) servletRequest).getPathInfo()).append("\n\r");
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            stringBuilder.append(header).append(": ").append(httpServletRequest.getHeader(header)).append("\n\r");
        }
        return stringBuilder.toString();
    }

    private boolean doLog(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getRequestURI().contains("service/image/");

    }

    private String getResponseHeaders(ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Collection<String> headers = httpServletResponse.getHeaderNames();
        StringBuilder stringBuilder = new StringBuilder();
        for (String header : headers) {
            stringBuilder.append(header).append(": ").append(httpServletResponse.getHeader(header)).append("\n\r");
        }
        return stringBuilder.toString();
    }
}
