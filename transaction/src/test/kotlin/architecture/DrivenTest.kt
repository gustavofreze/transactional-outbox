package architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Tag

@Tag("UnitTest")
@AnalyzeClasses(packages = ["driven"], importOptions = [DoNotIncludeTests::class])
class DrivenTest {

    @ArchTest
    fun `Independence of the framework`(importedClasses: JavaClasses) {
        val rule = noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..quarkus..")

        rule.check(importedClasses)
    }

    @ArchTest
    fun `Independence of the driver classes`(importedClasses: JavaClasses) {
        val rule = noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("driver..")

        rule.check(importedClasses)
    }
}
