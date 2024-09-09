package com.cae.cli.commands.projects;

import com.cae.command_controller.Command;
import com.cae.command_controller.CommandParameterDefinitions;
import com.cae.meta_structure.core.use_cases.generate_project_structure.implementations.GenerateProjectStructureUseCaseImplementation;
import com.cae.meta_structure.core.use_cases.generate_project_structure.io.inputs.GenerateProjectStructureUseCaseInput;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class NewProjectCommand extends Command {

    public NewProjectCommand() {
        super("new-project");
        this.registerParameter("artifactId", CommandParameterDefinitions.newRequiredDefaultParameter());
        this.registerParameter("groupId", CommandParameterDefinitions.newRequiredDefaultParameter());
        this.registerParameter("caeVersion", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var artifactId = this.getCommandParameters().get("artifactId").getActualValue();
        var groupId = this.getCommandParameters().get("groupId").getActualValue();
        var frameworkVersion = this.getCommandParameters().get("caeVersion").getActualValue();
        var useCaseInput = GenerateProjectStructureUseCaseInput.builder()
                .artifactId(artifactId)
                .groupId(groupId)
                .monolayer(true)
                .caeFrameworkVersion(frameworkVersion)
                .build();
        var useCase = new GenerateProjectStructureUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}
