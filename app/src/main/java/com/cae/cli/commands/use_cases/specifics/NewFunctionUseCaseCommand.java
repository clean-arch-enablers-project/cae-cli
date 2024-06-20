package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import com.cae.command_controller.CommandParameterDefinitions;
import com.cae.meta_structure.core.use_cases.generate_fuc_structure.implementations.GenerateFucUseCaseImplementation;
import com.cae.meta_structure.core.use_cases.generate_fuc_structure.io.inputs.GenerateFucUseCaseInput;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class NewFunctionUseCaseCommand extends NewUseCaseCommand {

    public NewFunctionUseCaseCommand() {
        super("new-fuc");
        this.registerParameter("name", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var name = this.getCommandParameters().get("name").getActualValue();
        var useCaseInput = GenerateFucUseCaseInput.builder()
                .name(name)
                .build();
        var useCase = new GenerateFucUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}
