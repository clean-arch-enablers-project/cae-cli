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


public class UpdateUseCaseCommand extends NewUseCaseCommand {

    protected UpdateUseCaseCommand() {
        super("update");
    }

    @Override
    protected void applyInternalLogic() {

    }

    public static List<String> fetchBranches(String owner, String repo) {
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

    public static void main(String[] args) {
        String owner = "clean-arch-enablers-project";
        String repo = "cae-cli";
        List<String> branches = fetchBranches(owner, repo);
        System.out.println("Branches disponíveis: " + branches);
    }
}