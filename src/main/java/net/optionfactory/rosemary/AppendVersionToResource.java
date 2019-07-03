package net.optionfactory.rosemary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AppendVersionToResource extends AbstractAttributeTagProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AppendVersionToResource.class);

    private static final String ATTR_NAME = "append";
    private static final int PRECEDENCE = 10000;
    private static final List<String> attributeNames = Arrays.asList("src", "href");
    private final Supplier<String> versionSupplier;

    public AppendVersionToResource(
            final String dialectPrefix,
            final Supplier<String> versionSupplier
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
        this.versionSupplier = versionSupplier;
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
                    ? String.format("%s&version=%s", content, versionSupplier.get())
                    : String.format("%s?version=%s", content, versionSupplier.get());
            structureHandler.setAttribute(attribute.getAttributeCompleteName(), versionedContent, attribute.getValueQuotes());
        } catch (Exception ex) {
            logger.error(String.format("Error while processing tag %s", tag.getElementCompleteName()), ex);
        }
    }
}
