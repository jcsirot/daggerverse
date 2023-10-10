package main

import "fmt"

type Java struct {
	Ctr *Container
}

func (java *Java) WithJdk(version string) *Java {
	if java.Ctr == nil {
		java.Ctr = dag.Container()
	}

	java.Ctr = java.Ctr.
		From(fmt.Sprintf("openjdk:%s", version)).
		WithExec([]string{"microdnf", "install", "git"})

	return java
}

func (java *Java) Container() *Container {
	return java.Ctr
}

func (java *Java) WithProject(source *Directory) *Java {
	java.Ctr = java.Ctr.
		WithWorkdir("/project").
		WithMountedDirectory("/project", source)

	return java
}
