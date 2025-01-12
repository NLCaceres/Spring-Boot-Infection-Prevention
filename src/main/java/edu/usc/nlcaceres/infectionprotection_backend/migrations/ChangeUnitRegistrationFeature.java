package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

// Idea taken from https://github.com/mongock/graalvm-example
public class ChangeUnitRegistrationFeature implements Feature {
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    Migrations.changeUnits.forEach(ChangeUnitRegistrationFeature::registerClass);
  }
  private static void registerClass(Class<?> clazz) {
    RuntimeReflection.register(clazz);
    RuntimeReflection.register(clazz.getDeclaredConstructors());
    RuntimeReflection.register(clazz.getDeclaredMethods());
  }
}
