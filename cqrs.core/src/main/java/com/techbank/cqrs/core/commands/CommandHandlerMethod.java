package com.techbank.cqrs.core.commands;
// an interface that has only one abstract method
@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {
    void handle(T command);

}
