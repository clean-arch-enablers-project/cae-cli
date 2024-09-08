package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import com.cae.command_controller.CommandParameterDefinitions;
import com.cae.meta_structure.core.use_cases.generate_suc_structure.implementations.GenerateSucUseCaseImplementation;
import com.cae.meta_structure.core.use_cases.generate_suc_structure.inputs.GenerateSucUseCaseInput;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class NewSupplierUseCaseCommand extends NewUseCaseCommand {

    public NewSupplierUseCaseCommand() {
        super("new-suc");
        this.registerParameter("name", CommandParameterDefinitions.newRequiredDefaultParameter());
        this.registerParameter("kotlin", CommandParameterDefinitions.newOptionalFlagParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var name = this.getCommandParameters().get("name").getActualValue();
        var kotlin = this.getCommandParameters().get("kotlin").isPresent();
        var useCaseInput = GenerateSucUseCaseInput.builder()
                .name(name)
                .kotlin(kotlin)
                .build();
        var useCase = new GenerateSucUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}
