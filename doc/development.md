# Development

## Update version

Update the version numbers of the following files.

```
build.gradle.kts
```

```
README.md
README.ja.md
```


Commit changes.

```shell
git add -A
git commit -m "Release v0.5.1"
git push origin main:main
```

By pushing a tag, the github action creates a release.

```shell
git tag v0.5.1
git push origin v0.5.1
```


## Maven publish

```shell
./gradlew publishAndReleaseToMavenCentral
```

[Central Portal](https://central.sonatype.com)


