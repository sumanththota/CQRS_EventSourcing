package com.techbank.cqrs.core.commands;

import com.techbank.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
public abstract class BaseCommand extends Message {
    //super will call the constructor of the parent class
    public BaseCommand(String id) {
        super(id);
    }

}
