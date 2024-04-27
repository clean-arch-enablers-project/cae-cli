from core.use_cases.java_web_use_case.ArchFlowJavaWeb import ArchFlowJavaWeb

directory_template = r"sua/pasta/raiz/dos/templates"

arch = ArchFlowJavaWeb()
arch.DirectoryExplorer.directory_template = directory_template

if __name__ == "__main__":
    args = arch.handle_args()
    arch.handler_functions_flow_java(args)
