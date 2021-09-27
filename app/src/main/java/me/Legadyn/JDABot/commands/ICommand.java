package me.Legadyn.JDABot.commands;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx) throws InterruptedException;

    String getName();

    default List<String> getAliases() {
        return List.of();
    }
}
