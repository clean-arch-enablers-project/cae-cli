package com.cae.cli.commands.use_cases.specifics;

import com.cae.cli.commands.use_cases.NewUseCaseCommand;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateUseCaseCommand extends NewUseCaseCommand {

    protected UpdateUseCaseCommand() {
        super("update");
    }

    private static final String GITHUB_OWNER = "ViniciusInTech";
    private static final String GITHUB_REPO = "clean-arch-enablers-CLI";
    private String latestBranch;

    @Override
    protected void applyInternalLogic() {
        try {
            validateEnvironment();
            List<String> branches = fetchBranches();
            latestBranch = findLatestVersionBranch(branches);

            if (latestBranch == null) {
                System.err.println("Não foi possível encontrar a última versão.");
                return;
            }

            System.out.println("Última branch encontrada: " + latestBranch);
            downloadAndExtractBranch();
            createAndRunUpdateScript();
        } catch (Exception e) {
            System.err.println("Erro durante o processo de atualização: " + e.getMessage());
        }
    }

    private void validateEnvironment() {
        String caeHome = System.getenv("CAE_CLI_HOME");
        if (caeHome == null || caeHome.isEmpty()) {
            throw new RuntimeException("Variável de ambiente CAE_CLI_HOME não configurada.");
        }
    }

    private List<String> fetchBranches() {
        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.github.com/repos/%s/%s/branches", GITHUB_OWNER, GITHUB_REPO);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Falha ao buscar branches. Código: " + response.code());
            }

            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            return root.findValuesAsText("name").stream()
                    .filter(branch -> branch.matches("^v\\d+\\.\\d+\\.\\d+$"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao acessar o repositório GitHub", e);
        }
    }

    private String findLatestVersionBranch(List<String> branches) {
        return branches.stream()
                .max((v1, v2) -> {
                    int[] parts1 = parseVersion(v1);
                    int[] parts2 = parseVersion(v2);

                    for (int i = 0; i < 3; i++) {
                        if (parts1[i] != parts2[i]) {
                            return Integer.compare(parts1[i], parts2[i]);
                        }
                    }
                    return 0;
                })
                .orElse(null);
    }

    private int[] parseVersion(String version) {
        String[] parts = version.substring(1).split("\\.");
        return new int[]{
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
        };
    }

    private void downloadAndExtractBranch() {
        String caeHome = System.getenv("CAE_CLI_HOME");
        String url = String.format("https://github.com/%s/%s/archive/refs/heads/%s.zip",
                GITHUB_OWNER, GITHUB_REPO, latestBranch);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Download falhou. Código: " + response.code());
            }

            File zipFile = new File(caeHome, latestBranch + ".zip");
            zipFile.getParentFile().mkdirs();

            Files.copy(response.body().byteStream(), zipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Download concluído: " + zipFile.getAbsolutePath());

            File destDir = new File(caeHome, "source");
            destDir.mkdirs();

            unzipFile(zipFile, destDir);
            System.out.println("Arquivos extraídos com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException("Erro no download da branch", e);
        }
    }

    private void unzipFile(File zipFile, File destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File destFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    destFile.getParentFile().mkdirs();
                    Files.copy(zis, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }

    private void createAndRunUpdateScript() {
        String caeHome = System.getenv("CAE_CLI_HOME");
        String batFilePath = new File(caeHome, "update-temp.bat").getAbsolutePath();

        // Caminho específico baseado na estrutura que você mencionou, removendo o prefixo "v"
        String versionWithoutV = latestBranch.startsWith("v") ? latestBranch.substring(1) : latestBranch;
        String sourceDir = new File(caeHome, "source\\clean-arch-enablers-CLI-" + versionWithoutV + "\\installer\\components").getAbsolutePath();

        try (FileWriter writer = new FileWriter(batFilePath)) {
            writer.write("@echo off\n");
            writer.write("echo Iniciando processo de atualização\n");
            writer.write("echo Versão: " + latestBranch + "\n");

            // Encerrar processos Java
            writer.write("echo Encerrando processos Java...\n");
            writer.write("taskkill /F /IM java.exe\n");

            // Copiar templates
            writer.write("echo Copiando templates...\n");
            writer.write("if exist \"" + sourceDir + "\\file-templates\" (\n");
            writer.write("    xcopy /E /I /H /Y \"" + sourceDir + "\\file-templates\" \"" + caeHome + "\\file-templates\"\n");
            writer.write("    if %errorlevel% equ 0 (\n");
            writer.write("        echo Templates copiados com sucesso\n");
            writer.write("    ) else (\n");
            writer.write("        echo Erro ao copiar templates: " + sourceDir + "\\file-templates para " + caeHome + "\\file-templates\n");
            writer.write("    )\n");
            writer.write(") else (\n");
            writer.write("    echo Diretório de templates não encontrado: " + sourceDir + "\\file-templates\n");
            writer.write(")\n");

            // Substituir JAR
            writer.write("echo Atualizando JAR...\n");
            writer.write("if exist \"" + sourceDir + "\\cae-cli.jar\" (\n");
            writer.write("    move /Y \"" + sourceDir + "\\cae-cli.jar\" \"" + caeHome + "\\cae-cli.jar\"\n");
            writer.write("    if %errorlevel% equ 0 (\n");
            writer.write("        echo JAR atualizado com sucesso\n");
            writer.write("    ) else (\n");
            writer.write("        echo Erro ao atualizar JAR\n");
            writer.write("    )\n");
            writer.write(") else (\n");
            writer.write("    echo Arquivo JAR não encontrado\n");
            writer.write(")\n");

            // Reiniciar aplicação
            writer.write("echo Reiniciando aplicação...\n");
            writer.write("java -jar \"" + caeHome + "\\cae-cli.jar\"\n");

            // Manter janela aberta para visualização
            writer.write("echo Atualização concluída\n");
            writer.write("echo Pressione qualquer tecla para fechar...\n");
            writer.write("pause >nul\n");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar script de atualização", e);
        }

        try {
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", batFilePath});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao executar script de atualização", e);
        }
    }

}