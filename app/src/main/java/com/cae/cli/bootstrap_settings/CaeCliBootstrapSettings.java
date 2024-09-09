package com.cae.cli.bootstrap_settings;

import com.cae.cli.commands.projects.NewProjectCommand;
import com.cae.cli.commands.use_cases.specifics.NewConsumerUseCaseCommand;
import com.cae.cli.commands.use_cases.specifics.NewFunctionUseCaseCommand;
import com.cae.cli.commands.use_cases.specifics.NewRunnableUseCaseCommand;
import com.cae.cli.commands.use_cases.specifics.NewSupplierUseCaseCommand;
import com.cae.command_controller.CommandRepository;
import com.cae.command_controller.client_settings.ClientSettingsProvider;
import com.cae.meta_structure.assemblers.logger.LoggerBootstrap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaeCliBootstrapSettings {

    public static void run(){
        ClientSettingsProvider.SINGLETON.setClientName("cae-cli");
        ClientSettingsProvider.SINGLETON.setVersion("1.0.0");
        LoggerBootstrap.startupSettings(new LoggerOverride());
        CommandRepository.SINGLETON.registerNewCommands(List.of(
                new NewFunctionUseCaseCommand(),
                new NewConsumerUseCaseCommand(),
                new NewSupplierUseCaseCommand(),
                new NewRunnableUseCaseCommand(),
                new NewProjectCommand()
        ));
    }

}
