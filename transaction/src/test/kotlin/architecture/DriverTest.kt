package architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Tag

@Tag("UnitTest")
@AnalyzeClasses(packages = ["driver"], importOptions = [DoNotIncludeTests::class])
class DriverTest {

    @ArchTest
    fun `Independence of the framework`(importedClasses: JavaClasses) {
        val rule = noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..quarkus..")

        rule.check(importedClasses)
    }

    @ArchTest
    fun `Independence of the driven classes`(importedClasses: JavaClasses) {
        val rule = noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("driven..")

        rule.check(importedClasses)
    }
}
