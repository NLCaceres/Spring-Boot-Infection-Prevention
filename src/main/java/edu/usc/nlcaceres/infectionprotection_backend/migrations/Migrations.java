package edu.usc.nlcaceres.infectionprotection_backend.migrations;

import java.util.Arrays;
import java.util.List;

public final class Migrations {
  public static final List<Class<?>> changeUnits = Arrays.asList(
    CreateProfessionsChangeUnit.class,
    CreateEmployeesChangeUnit.class,
    CreateLocationsChangeUnit.class,
    RenameLocationsFieldsChangeUnit.class,
    CreatePrecautionsChangeUnit.class,
    CreateHealthPracticesChangeUnit.class,
    CreateReportsChangeUnit.class,
    RenameReportsHealthPracticeFieldChangeUnit.class
  );
}
