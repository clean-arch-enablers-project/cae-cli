package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import com.cae.command_controller.CommandParameterDefinitions;
import com.cae.meta_structure.core.use_cases.generate_ruc_structure.implementations.GenerateRucUseCaseImplementation;
import com.cae.meta_structure.core.use_cases.generate_ruc_structure.inputs.GenerateRucUseCaseInput;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class NewRunnableUseCaseCommand extends NewUseCaseCommand {

    public NewRunnableUseCaseCommand() {
        super("new-ruc");
        this.registerParameter("name", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var name = this.getCommandParameters().get("name").getActualValue();
        var useCaseInput = GenerateRucUseCaseInput.builder()
                .name(name)
                .build();
        var useCase = new GenerateRucUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}
