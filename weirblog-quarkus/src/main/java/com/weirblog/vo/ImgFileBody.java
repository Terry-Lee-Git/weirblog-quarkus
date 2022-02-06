package com.weirblog.vo;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

public class ImgFileBody {

    @FormParam("imgFile")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputPart inputStream;

    @FormParam("name")
    @PartType(MediaType.TEXT_PLAIN)
    public String filePath;
}

