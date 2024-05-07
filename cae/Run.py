import os

from .ArchFlowJavaWeb import ArchFlowJavaWeb


def main():
    directory_template = os.path.join(os.path.dirname(os.path.abspath(__file__)), "templates")
    arch = ArchFlowJavaWeb()
    arch.DirectoryExplorer.directory_template = directory_template
    args = arch.handle_args()
    arch.handler_functions_flow_java(args)
