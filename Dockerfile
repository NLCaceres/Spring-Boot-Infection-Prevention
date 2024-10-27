# To find more GraalVM containers, check "ghcr.io/graalvm/jdk-community" or "ghcr.io/graalvm/graalvm-community"
FROM ghcr.io/graalvm/native-image-community:17-ol9 AS builder

# Having issues with `microdnf update`, possibly due to native-image preferred glibc version
# Not even `--best` option helps resolve the seemingly dependency issue
RUN microdnf install -y --nodocs findutils && microdnf clean all
# Better/simpler/common way to use/setup Linux package managers is the following
# RUN microdnf update -y \
#   && microdnf install -y --nodocs findutils \
#   && microdnf clean all

WORKDIR /app

COPY . /app/src/

WORKDIR /app/src
# Creates a runnable `build/native/nativeCompile/infectionprotection_backend`
RUN ./gradlew nativeCompile

FROM debian:stable-slim
EXPOSE 8080

COPY --from=builder --chmod=0755 /app/src/build/native/nativeCompile/infectionprotection_backend /app/infectionprotection_backend
CMD ["./app/infectionprotection_backend"]