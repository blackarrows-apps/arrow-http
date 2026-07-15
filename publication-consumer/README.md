# Published Apple consumer

This directory is a standalone Gradle build that verifies published Arrow HTTP Maven
coordinates. It intentionally contains no `mavenLocal()`, included build, project dependency,
or dependency substitution. Transitive dependencies resolve from Maven Central, while all
`io.github.blackarrows-apps` modules resolve exclusively from `arrowHttpRepository`.

Defaults:

- `arrowHttpVersion=1.3.0`
- `arrowHttpRepository=https://repo.maven.apache.org/maven2`

From the repository root on macOS:

```shell
./gradlew -p publication-consumer \
  -ParrowHttpRepository=https://your-staging-repository.example/releases \
  compileKotlinIosArm64 compileKotlinIosSimulatorArm64 iosSimulatorArm64Test
```

The command proves that the staged `1.3.0` metadata and Apple KLIBs resolve, compile, and
construct the Darwin client without making a request. To audit the old release, override
`-ParrowHttpVersion=1.2.0`; compilation fails because Central is missing the Apple target
modules advertised by its root metadata. The old iOS implementation also contains a shipped
`TODO`, but an external consumer cannot reach it because resolution fails first.

To verify a staged or promoted corrective release:

```shell
./gradlew -p publication-consumer \
  -ParrowHttpVersion=1.3.0 \
  -ParrowHttpRepository=https://your-staging-repository.example/releases \
  compileKotlinIosArm64 compileKotlinIosSimulatorArm64 iosSimulatorArm64Test
```

The smoke test performs no request. It constructs the public Darwin-backed client, supplies
it to `KtorHttpRequestExecutor`, and closes it.

The repository build provides a local Maven staging task suitable for the same verification:

```shell
./gradlew publishAllPublicationsToVerificationRepository
./gradlew -p publication-consumer \
  -ParrowHttpRepository="file://$PWD/build/verification-repository" \
  compileKotlinIosArm64 compileKotlinIosSimulatorArm64 iosSimulatorArm64Test
```
