from arch_flow import ArchFlow
from arch_flow import Filter
import subprocess
import os
import xml.etree.ElementTree as ET

filter = Filter()


class ArchFlowJavaWeb(ArchFlow):

    def __init__(self):
        super().__init__()
        self.StringManipulator.tag_functions_user = {"artifact_id": self.get_artifact_id,
                                                     "group_id": self.get_group_id}

    def create_project(self, group_id, artifact_id, version, package_name, dependencies=None):
        maven_command = [
            "mvn",
            "archetype:generate",  # Corrected the typo here
            "-DgroupId=" + group_id,
            "-DartifactId=" + artifact_id,
            "-Dversion=" + version,
            "-Dpackage=" + package_name,
            "-DarchetypeArtifactId=maven-archetype-quickstart",  # Corrected the prefix here
            "-DinteractiveMode=false"
        ]

        try:
            self.OutputHandler.information_message("starting to create a java project using maven")
            process = subprocess.Popen(" ".join(maven_command), shell=True, stdout=subprocess.PIPE,
                                       stderr=subprocess.STDOUT, text=True)

            for line in process.stdout:
                print(line, end='')

            process.wait()
            if process.returncode == 0:
                self.OutputHandler.success_message(f"Maven project '{artifact_id}' created successfully.")
            else:
                self.OutputHandler.alert_message(f"Error creating Maven project '{artifact_id}'. "
                                                 f"Return code: {process.returncode}")

        except Exception as e:
            self.OutputHandler.alert_message(f"Error executing Maven command: {e}")

    def functions_flow(self):
        return {
                "create_project": self.create_project,
                "-v": self.version,
                "--version": self.version,
                "clear_all": self.clear_all_project,
                "install_all_project": self.install_all_project,
                "go_up": self.go_up,
                }

    def clear_all_project(self):
        self.OutputHandler.information_message("starting cleaning of all projects")
        files_pom = self.DirectoryExplorer.list_files("pom.xml")
        for project in files_pom:
            self.clean_project(project)

    def go_up(self):
        try:
            os.chdir('..')
            self.OutputHandler.information_message("Directory changed to parent directory.")
        except FileNotFoundError:
            self.OutputHandler.alert_message("Parent directory not found.")
        except PermissionError:
            self.OutputHandler.alert_message("Permission denied to change directory.")

    def install_all_project(self):
        self.OutputHandler.information_message("starting installing of all projects")
        files_pom = self.DirectoryExplorer.list_files("pom.xml")
        for project in files_pom:
            self.install_project(project)

    def clean_project(self, root_project_pom):
        maven_command = f"mvn clean -f {root_project_pom}"
        try:
            subprocess.run(maven_command, shell=True, check=True)
            self.OutputHandler.success_message("Project clean successfully.")
        except subprocess.CalledProcessError as e:
            self.OutputHandler.alert_message(f"error when cleaning the project {e}")

    def install_project(self, root_project_pom):
        comando_maven = f"mvn install -f {root_project_pom}"
        try:
            subprocess.run(comando_maven, shell=True, check=True)
            self.OutputHandler.success_message("Project installing successfully.")
        except subprocess.CalledProcessError as e:
            self.OutputHandler.alert_message(f"error when installing a project {e}")

    @staticmethod
    def version():
        print("""
⠀⠀⠀⢠⣾⣷⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⣰⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⢰⣿⣿⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣤⣄⣀⣀⣤⣤⣶⣾⣿⣿⣿⡷
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⠀
⣿⣿⣿⡇⠀⡾⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠁⠀⠀⠀⠀⠀
⣿⣿⣿⣧⡀⠁⣀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠉⢹⠉⠙⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣀⠀⣀⣼⣿⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⠀⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⠀⠤⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⣿⣿⣿⣿⠿⣿⣿⣿⣿⣿⣿⣿⠿⠋⢃⠈⠢⡁⠒⠄⡀⠈⠁⠀⠀⠀⠀⠀⠀⠀
⣿⣿⠟⠁⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠈⠆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        """)
        print("⚡️ Cae Version 1.1 ⚡️")
        print("⚡️ ArchFlow Version 1.0 ⚡️")
        print("ArchFlow is evolving... slowly. But hey, we still love him! 🤣⚡️")

    def read_content_pom(self):
        poms = self.DirectoryExplorer.list_files("pom.xml")
        core_pom_path = filter.find_one_obj_by_key(poms, "Core")
        content = self.DirectoryExplorer.read_file(core_pom_path)
        return self.extract_content_pom(content)

    def get_artifact_id(self, input_string):
        group, artifact = self.read_content_pom()
        return artifact

    def get_group_id(self, input_string):
        group, artifact = self.read_content_pom()
        return group

    def extract_content_pom(self, content_pom):
        try:
            root = ET.fromstring(content_pom)

            group_id = root.find(".//groupId")
            artifact_id = root.find(".//artifactId")

            return (group_id.text if group_id is not None else None,
                    artifact_id.text if artifact_id is not None else None)

        except ET.ParseError:
            self.OutputHandler.alert_message("Error to open and reading file pom.")
            return None, None

    def handler_functions_flow_java(self, args):
        root_path_json = os.path.abspath(os.path.join(os.path.dirname(__file__), "db.json"))
        json_content = self.DirectoryExplorer.read_json_file(root_path_json)
        self.handler_input(args, json_content)
