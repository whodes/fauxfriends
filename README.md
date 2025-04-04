# fauxfriends

An app to find the people on Instagram that you are not following back.  
I am NOT a front-end developer, so the UI is not the best, but it works.  
You can see it in action at [fauxfriends.com](https://fauxfriends.com).

## Running the Application

### Prerequisites

Before running the app locally, make sure you have the following installed:
- [Java 17 or newer](https://adoptopenjdk.net/)
- [Gradle](https://gradle.org/)

### Run the application in development mode

To run the application in dev mode (live coding), use the following command:

```bash
./gradlew quarkusDev
```

This will start the application on [http://localhost:8080](http://localhost:8080), where you can access the app.

### Build and run the application

To build and run the application:

1. Build the app:

    ```bash
    ./gradlew build
    ```

2. Once built, run the app:

    ```bash
    java -jar build/quarkus-app/quarkus-run.jar
    ```

### Native executable (optional)

If you want to build a native executable for faster startup times, you can do that with:

```bash
./gradlew build -Dquarkus.native.enabled=true
```

Or, to build the native executable inside a container:

```bash
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

Once built, you can run the native executable:

```bash
./build/fauxfriends-1.0.0-SNAPSHOT-runner
```

For more information on creating native executables, check the [Quarkus Native Executable Guide](https://quarkus.io/guides/gradle-tooling).

## Related Guides

- [Quarkus REST Guide](https://quarkus.io/guides/rest): Learn about the Jakarta REST implementation in Quarkus.
