package com.cae.cli.autocomplete;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class AutocompleteGenerator {

    private static final Path COMMANDS_PATH = Paths.get("src/main/java/com/cae/cli/commands");
    private static final Path OUTPUT = Paths.get("target/cae_completion");

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> commandParams = new TreeMap<>();

        Files.walk(COMMANDS_PATH)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        List<String> lines = Files.readAllLines(path);
                        String commandName = extractCommandName(lines);
                        List<String> params = extractParams(lines);
                        if (commandName != null) {
                            commandParams.put(commandName, params);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        generateBashScript(commandParams);
    }

    private static String extractCommandName(List<String> lines) {
        Pattern pattern = Pattern.compile("super\\(\"([^\"]+)\"\\)");
        return lines.stream()
                .map(pattern::matcher)
                .filter(Matcher::find)
                .map(m -> m.group(1))
                .findFirst()
                .orElse(null);
    }

    private static List<String> extractParams(List<String> lines) {
        Pattern pattern = Pattern.compile("registerParameter\\(\"([^\"]+)\"");
        List<String> params = new ArrayList<>();
        lines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                params.add("--" + matcher.group(1));
            }
        });
        return params;
    }

    private static void generateBashScript(Map<String, List<String>> commandParams) throws IOException {
        Files.createDirectories(OUTPUT.getParent());

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(OUTPUT))) {
            writer.println("_cae_autocomplete() {");
            writer.println("    local cur prev cmd");
            writer.println("    COMPREPLY=()");
            writer.println("    cur=\"${COMP_WORDS[COMP_CWORD]}\"");
            writer.println("    cmd=\"${COMP_WORDS[1]}\"");

            writer.println("    declare -A args");
            for (Map.Entry<String, List<String>> entry : commandParams.entrySet()) {
                writer.printf("    args[\"%s\"]=\"%s\"%n", entry.getKey(), String.join(" ", entry.getValue()));
            }

            writer.printf("    commands=\"%s\"%n", String.join(" ", commandParams.keySet()));

            writer.println("    if [[ ${COMP_CWORD} == 1 ]]; then");
            writer.println("        COMPREPLY=( $(compgen -W \"$commands\" -- $cur) )");
            writer.println("        return 0");
            writer.println("    fi");

            writer.println("    if [[ -n \"${args[$cmd]}\" ]]; then");
            writer.println("        local used");
            writer.println("        for ((i=2; i<COMP_CWORD; i++)); do");
            writer.println("            used+=\" ${COMP_WORDS[i]}\"");
            writer.println("        done");
            writer.println("        local remaining=()");
            writer.println("        for opt in ${args[$cmd]}; do");
            writer.println("            if [[ ! $used =~ $opt ]]; then");
            writer.println("                remaining+=(\"$opt\")");
            writer.println("            fi");
            writer.println("        done");
            writer.println("        COMPREPLY=( $(compgen -W \"${remaining[*]}\" -- \"$cur\") )");
            writer.println("    fi");
            writer.println("}");
            writer.println("complete -F _cae_autocomplete cae");
        }
    }
}