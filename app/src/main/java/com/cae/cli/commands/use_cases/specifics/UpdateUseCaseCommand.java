package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateUseCaseCommand extends NewUseCaseCommand {

    protected UpdateUseCaseCommand() {
        super("update");
    }

    @Override
    protected void applyInternalLogic() {

    }

    private static List<String> fetchBranches(String owner, String repo) {
        List<String> branches = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.github.com/repos/%s/%s/branches", owner, repo);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);

                for (JsonNode branchNode : root) {
                    branches.add(branchNode.get("name").asText());
                }
            } else {
                System.err.println("error: " + response.message());
            }
        } catch (IOException e) {
            System.err.println("Failed to access the repository: " + e.getMessage());
        }
        return branches;
    }

    private static String lastestVersionBranch(List<String> branches){
        Pattern versionPattern = Pattern.compile("v(\\d+\\.\\d+\\.\\d+)");

        List<String> versionBranches = new ArrayList<>();
        for (String branch : branches) {
            Matcher matcher = versionPattern.matcher(branch);
            if (matcher.find()) {
                versionBranches.add(matcher.group(1));
            }
        }
        if (versionBranches.isEmpty()){
            return null;
        }
        return versionBranches.stream()
                .max(UpdateUseCaseCommand::compareVersions)
                .orElse(null);
    }

    private static int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        for (int i = 0; i < 3; i++) {
            int part1 = Integer.parseInt(parts1[i]);
            int part2 = Integer.parseInt(parts2[i]);
            if (part1 != part2) {
                return Integer.compare(part1, part2);
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String owner = "ViniciusInTech";
        String repo = "clean-arch-enablers-CLI";
        List<String> branches = fetchBranches(owner, repo);
        System.out.println("Branches disponíveis: " + branches);
        var branch = lastestVersionBranch(branches);
        System.out.println("versão mais recente " + branch);
    }
}