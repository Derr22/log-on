package controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class XMLTemplateConfig
{
    /* Constant(s): */
    /**
     * Path to root package/directory in which the different types of message
     * templates are found.
     */
    public static final String TEMPLATES_BASE = "classpath:/templates/";
    /** Pattern relative to templates base used to match XML templates. */
    public static final String XML_TEMPLATES_RESOLVE_PATTERN = "xml/*";
    /** Pattern relative to templates base used to match JSON templates. */
    public static final String JSON_TEMPLATES_RESOLVE_PATTERN = "json/*";
    /** Pattern relative to templates base used to match text templates. */
    public static final String HTML_TEMPLATES_RESOLVE_PATTERN = "html/*";


    /**
     * Creates the template resolver that retrieves XML message payloads.
     *
     * @return Template resolver.
     */
    @Bean
    public SpringResourceTemplateResolver xmlMessageTemplateResolver() {
        SpringResourceTemplateResolver theResourceTemplateResolver =
                new SpringResourceTemplateResolver();
        theResourceTemplateResolver.setPrefix(TEMPLATES_BASE);
        theResourceTemplateResolver.setResolvablePatterns(
                Collections.singleton(XML_TEMPLATES_RESOLVE_PATTERN));
        theResourceTemplateResolver.setSuffix(".xml");
        theResourceTemplateResolver.setTemplateMode("xml");
        theResourceTemplateResolver.setCharacterEncoding("UTF-8");
        theResourceTemplateResolver.setCacheable(false);
        theResourceTemplateResolver.setOrder(1);
        return theResourceTemplateResolver;
    }

    /**
     * Creates the template resolver that retrieves JSON message payloads.
     *
     * @return Template resolver.
     */
    @Bean
    public SpringResourceTemplateResolver jsonMessageTemplateResolver() {
        SpringResourceTemplateResolver theResourceTemplateResolver =
                new SpringResourceTemplateResolver();
        theResourceTemplateResolver.setPrefix(TEMPLATES_BASE);
        theResourceTemplateResolver.setResolvablePatterns(
                Collections.singleton(JSON_TEMPLATES_RESOLVE_PATTERN));
        theResourceTemplateResolver.setSuffix(".json");
        theResourceTemplateResolver.setTemplateMode("html");
        theResourceTemplateResolver.setCharacterEncoding("UTF-8");
        theResourceTemplateResolver.setCacheable(false);
        theResourceTemplateResolver.setOrder(2);
        return theResourceTemplateResolver;
    }

    /**
     * Creates the template resolver that retrieves text message payloads.
     *
     * @return Template resolver.
     */
    @Bean
    public SpringResourceTemplateResolver htmlMessageTemplateResolver() {
        SpringResourceTemplateResolver theResourceTemplateResolver =
                new SpringResourceTemplateResolver();
        theResourceTemplateResolver.setPrefix(TEMPLATES_BASE);
        theResourceTemplateResolver.setResolvablePatterns(
                Collections.singleton(HTML_TEMPLATES_RESOLVE_PATTERN));
        theResourceTemplateResolver.setSuffix(".html");
        theResourceTemplateResolver.setTemplateMode("html");
        theResourceTemplateResolver.setCharacterEncoding("UTF-8");
        theResourceTemplateResolver.setCacheable(false);
        theResourceTemplateResolver.setOrder(3);
        return theResourceTemplateResolver;
    }

    /**
     * Creates the template engine for all message templates.
     *
     * @param inTemplateResolvers Template resolver for different types of messages etc.
     * Note that any template resolvers defined elsewhere will also be included in this
     * collection.
     * @return Template engine.
     */
    @Bean
    public SpringTemplateEngine messageTemplateEngine(
            final Collection<SpringResourceTemplateResolver> inTemplateResolvers) {
        final SpringTemplateEngine theTemplateEngine = new SpringTemplateEngine();
        for (SpringResourceTemplateResolver theTemplateResolver : inTemplateResolvers) {
            theTemplateEngine.addTemplateResolver(theTemplateResolver);
        }
        return theTemplateEngine;
    }
}
