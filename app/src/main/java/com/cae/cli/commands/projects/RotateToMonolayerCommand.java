package com.cae.cli.commands.projects;

import com.cae.command_controller.Command;
import com.cae.meta_structure.assemblers.use_cases.rotate_to_monolayer.RotateToMonolayerUseCaseAssembler;
import com.cae.use_cases.correlations.UseCaseExecutionCorrelation;

public class RotateToMonolayerCommand extends Command {

    public RotateToMonolayerCommand() {
        super("rotate-to-monolayer");
    }

    @Override
    protected void applyInternalLogic() {
        RotateToMonolayerUseCaseAssembler.SINGLETON_ASSEMBLER.getDefaultAssembledInstance().execute(UseCaseExecutionCorrelation.ofNew());
    }
}
