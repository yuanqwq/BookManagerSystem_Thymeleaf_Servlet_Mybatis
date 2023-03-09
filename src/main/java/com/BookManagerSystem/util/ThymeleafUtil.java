package com.BookManagerSystem.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafUtil {
    private static final TemplateEngine engine;
    static {
        engine=new TemplateEngine();
        ClassLoaderTemplateResolver r=new ClassLoaderTemplateResolver();
        r.setCharacterEncoding("UTF-8");
        engine.setTemplateResolver(r);
    }
    public static TemplateEngine getEngine(){
        return engine;
    }
}
