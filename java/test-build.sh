#! /bin/sh

dagger="${_EXPERIMENTAL_DAGGER_CLI_BIN:-dagger}"

DIR_ID=$($dagger query --doc test/queries/get-demo-repository.gql --progress=plain | jq -r .git.branch.tree.id)

$dagger query --doc test/queries/maven-build.gql --var dirID=$DIR_ID