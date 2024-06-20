package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import com.cae.command_controller.CommandParameterDefinitions;
import com.cae.meta_structure.core.use_cases.generate_cuc_structure.implementations.GenerateCucUseCaseImplementation;
import com.cae.meta_structure.core.use_cases.generate_cuc_structure.inputs.GenerateCucUseCaseInput;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class NewConsumerUseCaseCommand extends NewUseCaseCommand {

    public NewConsumerUseCaseCommand() {
        super("new-cuc");
        this.registerParameter("name", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var name = this.getCommandParameters().get("name").getActualValue();
        var useCaseInput = GenerateCucUseCaseInput.builder()
                .name(name)
                .build();
        var useCase = new GenerateCucUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}
