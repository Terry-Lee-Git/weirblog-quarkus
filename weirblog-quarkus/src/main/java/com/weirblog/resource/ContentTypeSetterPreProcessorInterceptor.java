package com.weirblog.resource;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Provider
public class ContentTypeSetterPreProcessorInterceptor implements ContainerRequestFilter {
    private @Context HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        request.setAttribute(InputPart.DEFAULT_CHARSET_PROPERTY, StandardCharsets.UTF_8.name());
    }
}