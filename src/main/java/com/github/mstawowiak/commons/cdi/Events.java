package com.github.mstawowiak.commons.cdi;

import com.github.mstawowiak.commons.logging.SimpleLoggerFactory;
import java.lang.annotation.Annotation;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;

/**
 * Simplifies fire CDI events of a particular type.
 */
public final class Events {

    private static final Logger LOGGER = SimpleLoggerFactory.getLogger();

    /**
     * Fires an event with the specified qualifiers and notifies observers.
     *
     * @param event the event object
     * @param qualifiers the additional specified qualifiers
     */
    public static void fire(Object event, Annotation... qualifiers) {
        CDIContainer.getBeanManager().getEvent().select(qualifiers).fire(event);
    }

    /**
     * Fires an event asynchronously with the specified qualifiers and notifies asynchronous observers.
     *
     * @param event the event object
     * @param qualifiers the additional specified qualifiers
     * @return a {@link CompletionStage} allowing further pipeline composition on the asynchronous operation.
     */
    public static <U extends Object> CompletionStage<U> fireAsyncAndGet(U event, Annotation... qualifiers) {
        return CDIContainer.getBeanManager()
                .getEvent()
                .select(qualifiers)
                .fireAsync(event);
    }

    /**
     * Fires an event asynchronously, notifies asynchronous observers and logging exceptions raised by observers.
     *
     * @param event the event object
     * @param qualifiers the additional specified qualifiers
     */
    public static void fireAsync(Object event, Annotation... qualifiers) {
        fireAsyncAndGet(event, qualifiers)
                .exceptionally(throwable -> {
                    LOGGER.error("Error during handle event", throwable);
                    return null;
                });
    }

    private Events() {
    }

}
