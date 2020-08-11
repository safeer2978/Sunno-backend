package com.sunno.sunnozuulgateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class ZuulLoggingFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        logger.info("Request->{} request uri-> {}", request, request.getRequestURI());
        RequestContext ctx = RequestContext.getCurrentContext();

        // Alter ignored headers as per: https://gitter.im/spring-cloud/spring-cloud?at=56fea31f11ea211749c3ed22
        Set<String> headers = (Set<String>) ctx.get("ignoredHeaders");
        // We need our JWT tokens relayed to resource servers
        headers.remove("authorization");
        return null;
    }
}
