package com.cae.cli.bootstrap_settings;

import com.cae.loggers.Logger;

public class LoggerOverride implements Logger {

    @Override
    public void logInfo(String info) {
        //suppress
    }

    @Override
    public void logError(String error) {
        //suppress
    }

    @Override
    public void logDebug(String info) {
        //suppress
    }
}
