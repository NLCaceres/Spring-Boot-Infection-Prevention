FROM ghcr.io/graalvm/native-image-community:17-ol9 AS builder
# The following may work if using the "--static" and "--libc=musl" flags
# FROM ghcr.io/graalvm/native-image-community:17-muslib-ol9

RUN microdnf install -y --nodocs findutils && microdnf clean all

WORKDIR /app

COPY . /app/src/

WORKDIR /app/src

RUN ./gradlew nativeCompile
# Also possible to add build args like "--static" & "--libc" via gradle `build.gradle` config
# Ex (albeit overkill): "https://github.com/seqeralabs/tower-cli/blob/master/build.gradle"
# See also "https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html"
# Full command-line options: "https://www.graalvm.org/latest/reference-manual/native-image/overview/Options/"
# RUN ./gradlew nativeCompile --static --libc=musl