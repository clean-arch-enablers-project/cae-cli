package com.cae.cli;

import com.cae.cli.bootstrap_settings.CaeCliBootstrapSettings;
import com.cae.command_controller.CommandController;
import com.cae.command_controller.CommandRequest;

public class CaeCli {

    public static void main(String[] args) {
        CaeCliBootstrapSettings.run();
        CommandController.serve(CommandRequest.of(args));
    }

}