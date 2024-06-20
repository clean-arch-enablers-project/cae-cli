package com.cae.cli.commands.use_cases;

import com.cae.command_controller.Command;
import com.cae.command_controller.CommandParameterDefinitions;

import java.util.Optional;

public abstract class NewUseCaseCommand extends Command {

    protected NewUseCaseCommand(String name) {
        super(name);
        this.registerParameter("name", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    protected String getUseCaseName(){
        return Optional.ofNullable(this.getCommandParameters().get("name").getActualValue()).orElse("");
    }

}
