package net.optionfactory.rosemary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.Set;

public class VersionedResourceDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "version";
    private static final String DIALECT_PREFIX = "version";
    private static final int DIALECT_PRECEDENCE = 1000;
    private final String version;

    public VersionedResourceDialect(final String version) {
        super(DIALECT_NAME, DIALECT_PREFIX, DIALECT_PRECEDENCE);
        this.version = version;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return Collections.singleton(new AppendVersionToResource(dialectPrefix, version));
    }

}
