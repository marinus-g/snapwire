# SnapWire Dependency Injection Library

## Overview

SnapWire is a lightweight dependency injection (DI) Library for Java applications. It provides a simple and efficient way to manage dependencies and lifecycle of beans in your application.

## Features

- **Annotation-based Configuration**: Use annotations to define beans and their dependencies.
- **Custom DI Implementation**: Implement your own DI logic.
- **Cycle Detection**: Detect and handle circular dependencies.
- **Component Holders**: Manage components with custom holders.

## Getting Started

### Prerequisites

- Java 8 or higher
- Gradle

### Installation

1. Clone the repository:
    ```sh
    git clone git@github.com:marinus-g/snapwire.git
    cd snapwire
    ```

2. Build the project using Gradle:
    ```sh
    ./gradlew build
    ```

### Usage

#### Defining Beans

Use annotations to define your beans. Supported annotations include `@Service`, `@Component`, `@Configuration`, and `@Bean`.

Example:
```java
@Service
public class MyService {
    private final MyOtherService myOtherService;

    public MyService(MyOtherService myOtherService) {
        this.myRepository = myOtherService;
    }
}
```

#### Initializing the Context

Implement SnapWired in your main application class and call `register()` and `enable()` to initialize the context.

#### Accessing Beans

Retrieve beans from the context using their type or name.

Example:
```java
BeanContext context = SnapWire.getInstance().getRootContext();
MyService myService = context.getBean(MyService.class);
```

## Project Structure

- `src/main/java/dev/marinus/snapwire/annotation`: Contains custom annotations for defining beans.
- `src/main/java/dev/marinus/snapwire/context`: Core classes for managing the bean context and lifecycle.
- `src/main/java/dev/marinus/snapwire/context/details`: Classes representing details of beans.
- `src/main/java/dev/marinus/snapwire/validator`: Classes for validating bean configurations.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any questions or suggestions, please open an issue or contact the maintainer at [your-email@example.com].
