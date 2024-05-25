import os

from .ArchFlowJavaWeb import ArchFlowJavaWeb


def main(roots_path_json=None):
    if roots_path_json is None:
        roots_path_json = []
    roots_path_json.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "db.json")))
    directory_template = os.path.join(os.path.dirname(os.path.abspath(__file__)), "templates")
    arch = ArchFlowJavaWeb()
    arch.DirectoryExplorer.directory_template = directory_template
    args = arch.handle_args()
    arch.handler_functions_flow(args, roots_path_json)
