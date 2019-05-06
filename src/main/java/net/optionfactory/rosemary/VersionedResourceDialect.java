package net.optionfactory.rosemary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class VersionedResourceDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "version";
    private static final String DIALECT_PREFIX = "version";
    private static final int DIALECT_PRECEDENCE = 1000;
    
    private static final Logger logger = LoggerFactory.getLogger(VersionedResourceDialect.class);
    
    private final String version;

    public VersionedResourceDialect(final String version) {
        super(DIALECT_NAME, DIALECT_PREFIX, DIALECT_PRECEDENCE);
        this.version = version;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return Collections.singleton(new AppendVersionToResource(dialectPrefix, version));
    }

    public static class AppendVersionToResource extends AbstractAttributeTagProcessor {

        private static final String ATTR_NAME = "append";
        private static final int PRECEDENCE = 10000;
        private static final List<String> attributeNames = Arrays.asList("src", "href");
        private final String version;

        public AppendVersionToResource(
                final String dialectPrefix,
                final String version
        ) {
            super(
                    TemplateMode.HTML,
                    dialectPrefix,
                    null, // apply to all tags
                    false, // no prefix to be applied to tag name
                    ATTR_NAME,
                    true, // apply dialect prefix to attribute name
                    PRECEDENCE,
                    true // remove matched attribute afterwards
            );
            this.version = version;
        }

        @Override
        protected void doProcess(
                ITemplateContext context,
                IProcessableElementTag tag,
                AttributeName attributeName,
                String attributeValue,
                IElementTagStructureHandler structureHandler
        ) {
            try {
                final Optional<IAttribute> maybeAttribute = attributeNames.stream()
                        .map(tag::getAttribute)
                        .filter(t -> t != null)
                        .findFirst();
                if (!maybeAttribute.isPresent()) {
                    return;
                }
                final IAttribute attribute = maybeAttribute.get();
                final String content = attribute.getValue();
                if (content == null || content.isEmpty()) {
                    return;
                }
                if (content.contains("?version=") || content.contains("&version=")) {
                    return;
                }
                final String versionedContent = content.contains("?")
                        ? String.format("%s&version=%s", content, version)
                        : String.format("%s?version=%s", content, version);
                structureHandler.setAttribute(attribute.getAttributeCompleteName(), versionedContent, attribute.getValueQuotes());
            } catch (Exception ex) {
                logger.error(String.format("Error while processing tag %s", tag.getElementCompleteName()), ex);
            }
        }
    }
}
