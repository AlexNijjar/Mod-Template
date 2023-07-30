package tech.alexnijjar.modtemplate.steps

import com.intellij.ide.wizard.AbstractNewProjectWizardStep
import com.intellij.openapi.observable.util.bindBooleanStorage
import com.intellij.openapi.observable.util.bindStorage
import com.intellij.openapi.ui.validation.CHECK_NON_EMPTY
import com.intellij.openapi.ui.validation.WHEN_GRAPH_PROPAGATION_FINISHED
import com.intellij.ui.dsl.builder.*

class ModSettingsStep(parent: AbstractNewProjectWizardStep) : AbstractNewProjectWizardStep(parent) {

    private val nameProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.name")

    private val modIdProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.modId")

    private val groupProperty = propertyGraph.property("com.example.modid")
        .bindStorage("${javaClass.name}.group")

    private val versionProperty = propertyGraph.property("1.0.0")
        .bindStorage("${javaClass.name}.version")

    private val licenseProperty = propertyGraph.property("MIT")
        .bindStorage("${javaClass.name}.license")

    private val descriptionProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.description")

    private val authorsProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.authors")

    private val contributorsProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.contributors")

    private val websiteProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.website")

    private val repoProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.repo")

    private val issuesProperty = propertyGraph.property("")
        .bindStorage("${javaClass.name}.issues")

    private val datagenProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.datagen")

    private val clientCodeProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.clientCode")

    private val resourcefulLibProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.resourcefulLib")

    private val resourcefulConfigProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.resourcefulConfig")

    private val botariumProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.botarium")

    private val reiProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.rei")

    private val jeiProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.jei")

    private val geckolibProperty = propertyGraph.property(false)
        .bindBooleanStorage("${javaClass.name}.geckolib")

    val name by nameProperty
    val modId by modIdProperty
    val group by groupProperty
    val version by versionProperty
    val license by licenseProperty
    val description by descriptionProperty
    val authors by authorsProperty
    val contributors by contributorsProperty
    val website by websiteProperty
    val repo by repoProperty
    val issues by issuesProperty
    val datagen by datagenProperty
    val clientCode by clientCodeProperty
    val resourcefulLib by resourcefulLibProperty
    val resourcefulConfig by resourcefulConfigProperty
    val botarium by botariumProperty
    val rei by reiProperty
    val jei by jeiProperty
    val geckolib by geckolibProperty

    @Suppress("DialogTitleCapitalization")
    override fun setupUI(builder: Panel) {
        builder.group("Properties", false) {
            row("Name:") {
                textField()
                    .bindText(nameProperty)
                    .columns(COLUMNS_MEDIUM)
                    .validationRequestor(WHEN_GRAPH_PROPAGATION_FINISHED(propertyGraph))
                    .textValidation(CHECK_NON_EMPTY)
            }

            row("Mod ID:") {
                textField()
                    .bindText(modIdProperty)
                    .columns(COLUMNS_MEDIUM)
                    .validationRequestor(WHEN_GRAPH_PROPAGATION_FINISHED(propertyGraph))
                    .textValidation(CHECK_NON_EMPTY)
            }

            row("Group:") {
                textField()
                    .bindText(groupProperty)
                    .columns(COLUMNS_MEDIUM)
                    .validationRequestor(WHEN_GRAPH_PROPAGATION_FINISHED(propertyGraph))
                    .textValidation(CHECK_NON_EMPTY)
            }

            row("Version:") {
                textField()
                    .bindText(versionProperty)
                    .columns(COLUMNS_MEDIUM)
                    .validationRequestor(WHEN_GRAPH_PROPAGATION_FINISHED(propertyGraph))
                    .textValidation(CHECK_NON_EMPTY)
            }
        }

        builder.collapsibleGroup("Metadata", false) {
            row("License:") {
                textField()
                    .bindText(licenseProperty)
                    .columns(COLUMNS_MEDIUM)
                    .validationRequestor(WHEN_GRAPH_PROPAGATION_FINISHED(propertyGraph))
                    .textValidation(CHECK_NON_EMPTY)
            }

            row("Description:") {
                textField()
                    .bindText(descriptionProperty)
                    .columns(COLUMNS_MEDIUM)
            }

            row("Authors:") {
                textField()
                    .bindText(authorsProperty)
                    .columns(COLUMNS_MEDIUM)
            }

            row("Contributors:") {
                textField()
                    .bindText(contributorsProperty)
                    .columns(COLUMNS_MEDIUM)
            }

            row("Website:") {
                textField()
                    .bindText(websiteProperty)
                    .columns(COLUMNS_MEDIUM)
            }

            row("Repository:") {
                textField()
                    .bindText(repoProperty)
                    .columns(COLUMNS_MEDIUM)
            }

            row("Issues:") {
                textField()
                    .bindText(issuesProperty)
                    .columns(COLUMNS_MEDIUM)
            }
        }.expanded = true

        builder.collapsibleGroup("Code Options", false) {
            row {
                checkBox("Generate Datagen")
                    .bindSelected(datagenProperty)
            }

            row {
                checkBox("Generate Client Code")
                    .bindSelected(clientCodeProperty)
            }
        }.expanded = true

        builder.collapsibleGroup("Dependencies", false) {
            row {
                checkBox("Resourceful Lib")
                    .bindSelected(resourcefulLibProperty)
            }

            row {
                checkBox("Resourceful Config")
                    .bindSelected(resourcefulConfigProperty)
            }

            row {
                checkBox("Botarium")
                    .bindSelected(botariumProperty)
            }

            row {
                checkBox("Roughly Enough Items")
                    .bindSelected(reiProperty)
            }

            row {
                checkBox("Just Enough Items")
                    .bindSelected(jeiProperty)
            }

            row {
                checkBox("GeckoLib")
                    .bindSelected(geckolibProperty)
            }
        }
    }
}
