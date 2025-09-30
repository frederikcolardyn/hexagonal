package io.github.hexagonal.weather.bootstrap.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture tests to enforce hexagonal architecture rules.
 * These tests ensure the proper layering and dependency direction.
 */
class HexagonalArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setup() {
        classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.hexagonal.weather");
    }

    @Test
    void modelShouldNotDependOnAnyOtherLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..model..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..application..", "..adapter..", "..bootstrap..");

        rule.check(classes);
    }

    @Test
    void applicationShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapter..", "..bootstrap..");

        rule.check(classes);
    }

    @Test
    void applicationShouldDependOnModel() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application..")
            .should().onlyAccessClassesThat()
            .resideInAnyPackage(
                "..application..",
                "..model..",
                "java..",
                "lombok..",
                "org.jboss.logging.."
            );

        rule.check(classes);
    }

    @Test
    void adaptersShouldDependOnApplicationPorts() {
        ArchRule rule = classes()
            .that().resideInAPackage("..adapter..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..application.port..");

        rule.check(classes);
    }

    @Test
    void portsShouldBeInterfaces() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application.port..")
            .should().beInterfaces();

        rule.check(classes);
    }

    @Test
    void incomingPortsShouldEndWithUseCase() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application.port.in..")
            .should().haveSimpleNameEndingWith("UseCase");

        rule.check(classes);
    }

    @Test
    void outgoingPortsShouldNotEndWithUseCase() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..application.port.out..")
            .should().haveSimpleNameEndingWith("UseCase");

        rule.check(classes);
    }

    @Test
    void servicesShouldImplementUseCases() {
        // Verify that services depend on use case interfaces from port.in package
        ArchRule rule = classes()
            .that().resideInAPackage("..application.service..")
            .and().haveSimpleNameEndingWith("Service")
            .should().dependOnClassesThat()
            .resideInAPackage("..application.port.in..");

        rule.check(classes);
    }

    @Test
    void bootstrapCanDependOnAllLayers() {
        // Bootstrap is the composition root and can wire everything together
        ArchRule rule = classes()
            .that().resideInAPackage("..bootstrap..")
            .should().onlyAccessClassesThat()
            .resideInAnyPackage(
                "..bootstrap..",
                "..model..",
                "..application..",
                "..adapter..",
                "java..",
                "jakarta..",
                "io.quarkus..",
                "lombok..",
                "org.jboss.logging.."
            );

        rule.check(classes);
    }
}