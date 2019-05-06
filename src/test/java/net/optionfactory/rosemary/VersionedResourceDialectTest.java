package net.optionfactory.rosemary;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

public class VersionedResourceDialectTest {

    private static final String VERSION = "1";

    public TemplateEngine createEngine() {
        final TemplateEngine engine = new TemplateEngine();
        engine.addDialect(new VersionedResourceDialect("1"));
        return engine;
    }

    @Test
    public void appendsVersionToSrcAsQueryParameter() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<script src=\"/path/to/resource.js\" version:append></script>";
        final String expected = "<script src=\"/path/to/resource.js?version=1\"></script>";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void appendsVersionToSrcAsAnotherQueryParameterIfAnyExists() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<script src=\"/path/to/resource.js?param=a\" version:append></script>";
        final String expected = "<script src=\"/path/to/resource.js?param=a&version=1\"></script>";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void appendsVersionToHrefAsQueryParameter() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<link href=\"/path/to/resource.js\" version:append>";
        final String expected = "<link href=\"/path/to/resource.js?version=1\">";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void appendsVersionToHrefAsAnotherQueryParameterIfAnyExists() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<link href=\"/path/to/resource.js?param=a\" version:append>";
        final String expected = "<link href=\"/path/to/resource.js?param=a&version=1\">";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ifNoHrefOrSrcIsPresentDoNothing() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<div version:append></div>";
        final String expected = "<div></div>";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }

    @Test
    public void ifSrcIsEmptyDoNothing() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<script src=\"\" version:append></script>";
        final String expected = "<script src=\"\"></script>";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void ifHrefIsEmptyDoNothing() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<link href=\"\" version:append>";
        final String expected = "<link href=\"\">";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void ifVersionAlreadyPresentDoNothing() {
        final TemplateEngine engine = createEngine();
        final IContext context = new Context();
        final String template = "<script src=\"/path/to/resource.js?version=2\" version:append></script>";
        final String expected = "<script src=\"/path/to/resource.js?version=2\"></script>";
        final String got = engine.process(template, context);
        Assert.assertEquals(expected, got);
    }
}
