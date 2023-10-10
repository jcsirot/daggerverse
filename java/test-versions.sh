#! /bin/sh

dagger="${_EXPERIMENTAL_DAGGER_CLI_BIN:-dagger}"
jdk="${JDK_VERSION:-17}"
maven="${MVN_VERSION:-3.9.5}"

container_id=$($dagger query --doc test/queries/java-maven-container.gql --progress=plain --var jdk=$jdk --var maven=$maven | jq -r ".java.withJdk.withMaven.container.id")

java_version=$($dagger query --doc test/queries/java-version.gql --var id=$container_id -s --progress=plain | jq -r ".container.withExec.stderr")
mvn_version=$($dagger query --doc test/queries/maven-version.gql --var id=$container_id -s --progress=plain | jq -r ".container.withExec.stdout")

echo $java_version
echo $mvn_version