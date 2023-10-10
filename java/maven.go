package main

import (
	"fmt"
)

func (java *Java) WithMaven(version string) *Java {
	url := fmt.Sprintf("https://dlcdn.apache.org/maven/maven-3/%s/binaries/apache-maven-%s-bin.tar.gz", version, version)
	targetDir := fmt.Sprintf("/opt/apache-maven-%s", version)
	java.Ctr = java.Ctr.
		WithMountedCache("/root/.m2", dag.CacheVolume("maven-cache")).
		WithExec([]string{"curl", url, "-o", "/root/maven.tar.gz"}).
		WithExec([]string{"tar", "-xzvf", "/root/maven.tar.gz", "-C", "/opt"}).
		WithEnvVariable("M2_HOME", targetDir).
		WithEnvVariable("PATH", fmt.Sprintf("%s/bin:$PATH", targetDir), ContainerWithEnvVariableOpts{
			Expand: true,
		})
	return java
}

func (java *Java) Run(args []string) *Container {
	return java.Ctr.WithExec(args)
}

func (java *Java) Maven(args []string) *Container {
	cmd := append([]string{"mvn"}, args...)
	return java.Run(cmd)
}
