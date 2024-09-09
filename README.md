# ✔️ cae-cli
☕ Java & Kotlin edition

<br>

Welcome to the open source CAE CLI tool repository! The _cae-cli_ is designed to make the experience of applying the _cae-framework_ effortless. With one simple command you can generate the whole structure of a new use case in your Java or Kotlin project, already including its components throughout the application layers (core, adapters and assemblers).

<br>

State Symbol Key:

- ``✅`` — _Under release state_
- ``✔️`` — _Under snapshot state_
- ``⏳`` — _Under full development state_

<br>

## 🔧 How to install it
1. Clone the project.
2. Run the ``cae-cli-installer.exe`` file.
3. Add ``%CAE_CLI_HOME%`` to your system's or user's Path environment variable.
4. To test the installation, open a command prompt and run ``cae ls``.

Expected result:

![image](https://github.com/user-attachments/assets/65e0410a-f283-42ae-b5e1-399abeff713b)

<br>

## ▶️ Using it
To run any command (except for ``new-project``), you must be in the root directory of a CAE project. A CAE project is defined as a Java or Kotlin project that contains a ``cae-config.json`` file in its root directory. The format of this file is as follows:

```json
{
    "organization": "br.com.stockio",
    "domain": "empresas",
    "monolayer": true,
    "caeVersion": "0.11.0",
    "useCasePaths": [
        {
            "layer": "core",
            "location": "src/main/java/br/com/stockio/empresas/core/use_cases"
        },
        {
            "layer": "adapters",
            "location": "src/main/java/br/com/stockio/empresas/adapters/use_cases"
        },
        {
            "layer": "assemblers",
            "location": "src/main/java/br/com/stockio/empresas/assemblers/use_cases"
        }
    ]
}
````
- ``organization``: your groupId
- ``domain``: your artifactId
- ``monolayer``: if the project is structured as a single unit or divided into smaller subprojects
- ``caeVersion``: cae-framework version being currently used
- ``useCasePaths``: path the CLI will use to find use cases at the core, adapters and assemblers layers

<br>

### ``✅`` ``cae new-fuc``
Run this command for creating a new ``FunctionUseCase`` declaration in your CAE project.

Accepted parameters:
- **name**: the name of the use case
- **kotlin**: whether or not it should be generated in Kotlin

Expected effect:
```bash
├── core/                                                     # Core layer package
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # The new use case package
│   │   │   ├── SomeExampleUseCase.java/kt                    # Abstract class for the primary port of the new use case
│   │   │   ├── implementations/                              # Package for the implementation of the new use case
│   │   │   │   └── SomeExampleUseCaseImplementation.java/kt  # Implementation class of the new use case
│   │   │   ├── io/                                           # Package for the I/O declaration of the use case
│   │   │   │   ├── inputs/                                   # Package for input classes
│   │   │   │   │   └── SomeExampleUseCaseInput.java/kt       # Main input class for the new use case
│   │   │   │   └── output/                                   # Package for output classes
│   │   │   │       └── SomeExampleUseCaseOutput.java/kt      # Main output class for the new use case

├── adapters/                                                 # Adapters layer package
│   ├── use_cases/                                            # Use cases package
│   │   └── some_example/                                     # Package for the new use case adapters (starts empty)

├── assemblers/                                               # Assemblers layer
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # Package for assembling the new use case
│   │   │   └── MyUseCaseAssembler.java/kt                    # Assembler class (Factory) for the use case

```

<br>

### ``✅`` ``cae new-cuc``
Run this command for creating a new ``ConsumerUseCase`` declaration in your CAE project

Accepted parameters:
- **name**: the name of the use case
- **kotlin**: whether or not it should be generated in Kotlin

Expected effect:
```bash
├── core/                                                     # Core layer package
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # The new use case package
│   │   │   ├── SomeExampleUseCase.java/kt                    # Abstract class for the primary port of the new use case
│   │   │   ├── implementations/                              # Package for the implementation of the new use case
│   │   │   │   └── SomeExampleUseCaseImplementation.java/kt  # Implementation class of the new use case
│   │   │   ├── io/                                           # Package for the I/O declaration of the use case
│   │   │   │   ├── inputs/                                   # Package for input classes
│   │   │   │   │   └── SomeExampleUseCaseInput.java/kt       # Main input class for the new use case

├── adapters/                                                 # Adapters layer package
│   ├── use_cases/                                            # Use cases package
│   │   └── some_example/                                     # Package for the new use case adapters (starts empty)

├── assemblers/                                               # Assemblers layer
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # Package for assembling the new use case
│   │   │   └── MyUseCaseAssembler.java/kt                    # Assembler class (Factory) for the use case

```
<br>

### ``✅`` ``cae new-suc``
Run this command for creating a new ``SupplierUseCase`` declaration in your CAE project


Accepted parameters:
- **name**: the name of the use case
- **kotlin**: whether or not it should be generated in Kotlin

Expected effect:
```bash
├── core/                                                     # Core layer package
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # The new use case package
│   │   │   ├── SomeExampleUseCase.java/kt                    # Abstract class for the primary port of the new use case
│   │   │   ├── implementations/                              # Package for the implementation of the new use case
│   │   │   │   └── SomeExampleUseCaseImplementation.java/kt  # Implementation class of the new use case
│   │   │   ├── io/                                           # Package for the I/O declaration of the use case
│   │   │   │   ├── outputs/                                  # Package for output classes
│   │   │   │   │   └── SomeExampleUseCaseOutput.java/kt      # Main output class for the new use case

├── adapters/                                                 # Adapters layer package
│   ├── use_cases/                                            # Use cases package
│   │   └── some_example/                                     # Package for the new use case adapters (starts empty)

├── assemblers/                                               # Assemblers layer
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # Package for assembling the new use case
│   │   │   └── MyUseCaseAssembler.java/kt                    # Assembler class (Factory) for the use case

```
<br>

### ``✅`` ``cae new-ruc``
Run this command for creating a new ``RunnableUseCase`` declaration in your CAE project

Accepted parameters:
- **name**: the name of the use case
- **kotlin**: whether or not it should be generated in Kotlin

Expected effect:
```bash
├── core/                                                     # Core layer package
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # The new use case package
│   │   │   ├── SomeExampleUseCase.java/kt                    # Abstract class for the primary port of the new use case
│   │   │   ├── implementations/                              # Package for the implementation of the new use case
│   │   │   │   └── SomeExampleUseCaseImplementation.java/kt  # Implementation class of the new use case

├── adapters/                                                 # Adapters layer package
│   ├── use_cases/                                            # Use cases package
│   │   └── some_example/                                     # Package for the new use case adapters (starts empty)

├── assemblers/                                               # Assemblers layer
│   ├── use_cases/                                            # Use cases package
│   │   ├── some_example/                                     # Package for assembling the new use case
│   │   │   └── MyUseCaseAssembler.java/kt                    # Assembler class (Factory) for the use case

```

<br>

## 🌐 Other components of the SDK:

- ``✔️`` [cae-framework](https://github.com/clean-arch-enablers-project/cae-framework)
- ``✔️`` [cae-utils-mapped-exceptions](https://github.com/clean-arch-enablers-project/cae-utils-mapped-exceptions)
- ``✔️`` [cae-utils-http-client](https://github.com/clean-arch-enablers-project/cae-utils-http-client)
- ``✔️`` [cae-common-primary-adapters](https://github.com/clean-arch-enablers-project/cae-common-primary-adapters)
- ``✔️`` [cae-utils-env-vars](https://github.com/clean-arch-enablers-project/cae-utils-env-vars)
- ``✔️`` [cae-utils-trier](https://github.com/clean-arch-enablers-project/cae-utils-trier)
- ``⏳`` [cae-service-catalog](https://github.com/clean-arch-enablers-project/cae-service-catalog)

<br>
<br>
<br>
<br>

<p align="center">
  CAE — Clean Architecture made easy.
</p>
