package architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Tag

@Tag("UnitTest")
@AnalyzeClasses(packages = ["domain"], importOptions = [DoNotIncludeTests::class])
class DomainTest {

    @ArchTest
    fun `Inner dependencies`(importedClasses: JavaClasses) {
        val rule = classes()
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage("java..", "javax..", "kotlin..", "domain..", "arrow..", "org.jetbrains..")
            .because("Não devem depender da estrutura e/ou classes de infraestrutura")

        rule.check(importedClasses)
    }
}
