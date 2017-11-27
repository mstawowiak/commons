package com.github.mstawowiak.commons.cdi.startup;

import com.github.mstawowiak.commons.cdi.CDIContainer;
import com.github.mstawowiak.commons.logging.SimpleLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.slf4j.Logger;

/**
 * Extension for startup beans with annotations {@link Startup}
 */
@Vetoed
public class StartupExtension implements Extension {

    private static final Logger LOGGER = SimpleLoggerFactory.getLogger();

    private final List<StartupBean<?>> toStart = new ArrayList<>();

    public <T> void processAnnotatedType(@Observes @WithAnnotations(Startup.class) ProcessAnnotatedType<T> type) {
        Startup startup = type.getAnnotatedType().getAnnotation(Startup.class);

        toStart.add(
                new StartupBean<>(
                        startup.priority(),
                        startup.blocking(),
                        type.getAnnotatedType().getJavaClass()));
    }

    public void afterDeploymentValidation(@Observes @Initialized(ApplicationScoped.class) Object init) {
        Collections.sort(toStart, Comparator.comparingInt(StartupBean::getPriority));

        toStart.forEach(bean -> start(bean));
        toStart.clear();
    }

    private <T> void start(StartupBean<T> startup) {
        try {
            CDIContainer.select(startup.getClazz(), new Any.Literal()).toString();
        } catch (Exception ex) {
            LOGGER.error("Error during startup component " + startup.getClazz().getName(), ex);
            if (startup.isBlocking()) {
                throw ex;
            }
        }
    }

    private class StartupBean<T> {

        private final int priority;
        private final boolean blocking;
        private final Class<T> clazz;

        StartupBean(int priority, boolean blocking, Class<T> clazz) {
            this.priority = priority;
            this.blocking = blocking;
            this.clazz = clazz;
        }

        public int getPriority() {
            return priority;
        }

        public boolean isBlocking() {
            return blocking;
        }

        public Class<T> getClazz() {
            return clazz;
        }
    }
}
