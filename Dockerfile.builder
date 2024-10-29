# This image has the `musl` toolchain to enable "--static --libc=musl" by adding it in `build.gradle` config
FROM ghcr.io/graalvm/native-image-community:17-muslib-ol9 AS builder

RUN microdnf install -y --nodocs findutils && microdnf clean all

WORKDIR /app

COPY . /app/src/

WORKDIR /app/src

RUN ./gradlew nativeCompile
# Examples of setting up `nativeCompile` via gradle:
# Possibly overkill but detailed: "https://github.com/seqeralabs/tower-cli/blob/master/build.gradle"
# See also: "https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html"
# Full command-line options: "https://www.graalvm.org/latest/reference-manual/native-image/overview/Options/"