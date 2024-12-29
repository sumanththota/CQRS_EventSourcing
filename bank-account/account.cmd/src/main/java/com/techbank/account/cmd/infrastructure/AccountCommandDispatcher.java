package com.techbank.account.cmd.infrastructure;


import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    //representation of the collection of command handler methods
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>>  routes = new HashMap<>();

    // we are adding command handler methods to our routes map(registring a new command handler)
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.size() == 0){
            throw new RuntimeException("No command handler was register");

        }
        if (handlers.size() > 1){
            throw new RuntimeException("Cannot send command to more than one handler!");
        }
        handlers.get(0).handle(command);

    }
}
