package com.weirblog.vo.page;

import io.quarkus.qute.TemplateExtension;

@TemplateExtension
public class TemplateExtensions {

    public static Integer addOne(PageView<Object> item) { 
        return item.getCurrentPage() + 1;
    }
    public static Integer minusOne(PageView<Object> item) { 
    	return item.getCurrentPage() - 1;
    }
}