# Nomad Realms

Nomad Realms is a game where you explore, build, and survive in a tile-based procedurally generated world. You play as a nomad who must gather resources, craft items, and build structures to survive and thrive in the world. The game features a real-time card action system, where you use cards to perform actions such as moving, attacking, and building, and otherwise interacting with the world.

## Installation

### Prerequisites

- Java 11 or higher
- Maven

### Steps

1. Clone the repository:
   ```
   git clone https://github.com/virtual-cardboard/nomad-realms.git
   ```
2. Navigate to the project directory:
   ```
   cd nomad-realms
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

## Releases

New releases are automatically created when a tag starting with `v` is pushed to the repository.

To trigger a new release:
1. Create a new tag (e.g., `v0.0.1`):
   ```
   git tag v0.0.1
   ```
2. Push the tag to GitHub:
   ```
   git push origin v0.0.1
   ```

Alternatively, you can trigger a release manually from the GitHub Actions tab by selecting the "Create Release" workflow and providing a tag name.

## Development with IntelliJ IDEA

To ensure that IntelliJ IDEA correctly identifies the generated classes from the `@Derializable` annotation processor, follow these steps:

1. **Enable Annotation Processing:**
    - Go to `File` > `Settings` (or `IntelliJ IDEA` > `Settings` on macOS).
    - Navigate to `Build, Execution, Deployment` > `Compiler` > `Annotation Processors`.
    - Check **Enable annotation processing**.
    - Ensure "Obtain processors from project classpath" is selected.

2. **Configure Generated Sources Directory:**
    - In the same settings page, ensure **Store generated sources at:** is set to a valid path (usually `target/generated-sources/annotations` by default for Maven projects).
    - If IntelliJ doesn't automatically pick up the generated sources, right-click on the `target/generated-sources/annotations` folder in the Project view and select **Mark Directory as** > **Generated Sources Root**.

3. **Reload Maven Project:**
    - Open the Maven tool window and click the **Reload All Maven Projects** button to ensure the `pom.xml` changes are correctly applied.
