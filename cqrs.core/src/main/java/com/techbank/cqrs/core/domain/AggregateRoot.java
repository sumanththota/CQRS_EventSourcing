package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.events.BaseEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    //this will contain all the changes that are made to aggregate in the form of events
    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());


    public String getId() {
        return this.id;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    // fetch the uncommited changes
    public List<BaseEvent> getUncommitedChanges() {
        return this.changes;
    }
    // flush out all the changes once they are committed

    public void markChangesAsCommitted() {
        this.changes.clear();
    }
    // protected: we don't want the outside world to directly call this method
    // only its subclass can call this method
    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, MessageFormat.format("The apply method was not found in the aggregate for {0}", event.getClass()));

        } catch (Exception e) {
            logger.log(Level.SEVERE, MessageFormat.format("An error occurred while applying the event {0} to the aggregate", event.getClass()));
        } finally {
            if (isNewEvent) {
                changes.add(event);
            }
        }
    }
    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }
    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event -> applyChange(event, false));
    }



}
