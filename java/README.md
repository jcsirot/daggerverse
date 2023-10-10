# Daggerverse Java Module

This module setup an OpenJDK dev environment, optionnaly with Maven

## Usage

You can define a development environment with Java and Maven

```graphql
query build {
    java {
        withJdk(version: "17") {
            withMaven(version: "3.9.5") {
                container {
                    withExec(args: ["mvn", "-version"]) {
                        stdout
                    }
                }
            }
        }
    }
}
```

### How to build a Github repository

Fetch the repository

```graphql
query  {
    git(url: "github.com/my-user/my-repo/") {
        branch(name: "main") {
            tree {
                id
            }
        }
    }
}
```

And run a maven command

```graphql
query build ($dirID: DirectoryID!) {
    java {
        withJdk(version: "17") {
            withMaven(version: "3.9.5") {
                withProject(source: $dirID) {
                    maven(args: ["clean", "verify"]) {
                        stdout
                    }
                }
            }
        }
    }
}
```

## Examples

`test-version.sh` and `test-build.sh` provide some module usgae examples