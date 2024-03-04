package tech.alexnijjar.modtemplate

import com.intellij.icons.AllIcons
import com.intellij.ide.projectWizard.generators.AssetsNewProjectWizardStep
import com.intellij.ide.starters.local.StandardAssetsProvider
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.AbstractNewProjectWizardBuilder
import com.intellij.ide.wizard.GitNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardBaseStep
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import com.intellij.openapi.project.Project
import tech.alexnijjar.modtemplate.steps.ModSettingsStep

// TODO: get versions from separate thread
// TODO: format after all files have been added
// TODO: add licence file templates
// TODO: fix authors and contributor list on fabric.mod.json
class ModTemplateBuilder : AbstractNewProjectWizardBuilder() {
    override fun getDescription() = "Tool for creating multiplatform mods."
    override fun getNodeIcon() = AllIcons.General.Gear
    override fun getPresentableName() = "Mod Template"

    override fun createStep(context: WizardContext) =
            RootNewProjectWizardStep(context)
                    .nextStep(::NewProjectWizardBaseStep)
                    .nextStep(::GitNewProjectWizardStep)
                    .nextStep(::ModSettingsStep)
                    .nextStep(::AssetsStep)

    @Suppress("UnstableApiUsage")
    class AssetsStep(private val parent: ModSettingsStep) : AssetsNewProjectWizardStep(parent) {

        override fun setupAssets(project: Project) {
            val modName = parent.name
            val modid = parent.modId
            val group = parent.group
            val version = parent.version
            val minecraftVersion = parent.minecraftVersion

            val license = parent.license
            val description = parent.description
            val authors = parent.authors
            val contributors = parent.contributors
            val website = parent.website
            val repo = parent.repo
            val issues = parent.issues

            val datagen = parent.datagen
            val clientCode = parent.clientCode

            val resourcefulLib = parent.resourcefulLib
            val resourcefulConfig = parent.resourcefulConfig
            val botarium = parent.botarium
            val rei = parent.rei
            val jei = parent.jei
            val geckolib = parent.geckolib

            val className = modName.replace(" ", "")
            val groupPath = group.replace(".", "/")

            val gradleVersion = VersionUtils.gradle()
            val parchmentVersion = VersionUtils.parchment(minecraftVersion)
            val archLoomVersion = VersionUtils.archLoom()
            val modMenuVersion = VersionUtils.modrinth("modmenu", minecraftVersion)
            val commonAtsVersion = "2.0"
            val fabricLoaderVersion = VersionUtils.fabricLoader()
            val fabricApiVersion = VersionUtils.modrinth("fabric-api", minecraftVersion).split("+")[0]

            // TODO: fix this to pull the correct neo version for the correct minecraft version
            val neoForgeVersion = VersionUtils.neoForge()

            val templates = mutableMapOf(
                    "MOD_NAME" to modName,
                    "MOD_ID" to modid,
                    "GROUP" to group,
                    "VERSION" to version,
                    "MINECRAFT_VERSION" to minecraftVersion,

                    "LICENSE" to license,
                    "DESCRIPTION" to description,
                    "AUTHORS" to authors,
                    "CONTRIBUTORS" to contributors,
                    "WEBSITE" to website,
                    "REPO" to repo,
                    "ISSUES" to issues,

                    "HAS_DATAGEN" to datagen,
                    "HAS_CLIENT_CODE" to clientCode,

                    "HAS_RESOURCEFUL_LIB" to resourcefulLib,
                    "HAS_RESOURCEFUL_CONFIG" to resourcefulConfig,
                    "HAS_BOTARIUM" to botarium,
                    "HAS_REI" to rei,
                    "HAS_JEI" to jei,
                    "HAS_GECKOLIB" to geckolib,

                    "CLASS_NAME" to className,
                    "GROUP_PATH" to groupPath,

                    "GRADLE_VERSION" to gradleVersion,
                    "PARCHMENT_VERSION" to parchmentVersion,
                    "ARCH_LOOM_VERSION" to archLoomVersion,
                    "MOD_MENU_VERSION" to modMenuVersion,
                    "COMMON_ATS_VERSION" to commonAtsVersion,
                    "FABRIC_LOADER_VERSION" to fabricLoaderVersion,
                    "FABRIC_API_VERSION" to fabricApiVersion,
                    "NEO_FORGE_VERSION" to neoForgeVersion,
            )

            if (resourcefulLib) {
                templates["RESOURCEFUL_LIB_VERSION"] = VersionUtils.modrinth("resourceful-lib", minecraftVersion)
            }
            if (resourcefulConfig) {
                templates["RESOURCEFUL_CONFIG_VERSION"] = VersionUtils.modrinth("resourceful-config", minecraftVersion)
            }
            if (botarium) {
                templates["BOTARIUM_VERSION"] = VersionUtils.modrinth("botarium", minecraftVersion)
            }
            if (rei) {
                templates["REI_VERSION"] = VersionUtils.modrinth("rei", minecraftVersion).split("+")[0]
            }
            if (jei) {
                templates["JEI_VERSION"] = VersionUtils.modrinth("jei", minecraftVersion)
            }
            if (geckolib) {
                templates["GECKOLIB_VERSION"] = VersionUtils.modrinth("geckolib", minecraftVersion)
            }

            addAssets(StandardAssetsProvider().getGradlewAssets())

            addTemplateAsset(".github/workflows/release.yml", "release.yml", templates)
            addTemplateAsset(".idea/codeStyles/codeStyleConfig.xml", "codeStyleConfig.xml", templates)
            addTemplateAsset(".idea/codeStyles/Project.xml", "Project.xml", templates)
            addTemplateAsset("gradle/wrapper/gradle-wrapper.properties", "gradle-wrapper.properties", templates)
            addTemplateAsset(".gitignore", ".gitignore", templates)
            addTemplateAsset("build.gradle.kts", "build.gradle.kts", templates)
            addTemplateAsset("changelog.md", "changelog.md", templates)
            addTemplateAsset("gradle.properties", "gradle.properties", templates)
            addTemplateAsset("README.md", "README.md", templates)
            addTemplateAsset("settings.gradle.kts", "settings.gradle.kts", templates)

            // Common
            addTemplateAsset("common/src/main/java/$groupPath/$className.java", "CommonMain.java", templates)
            if (clientCode) addTemplateAsset("common/src/main/java/$groupPath/client/${className}Client.java", "CommonModClient.java", templates)
            if (resourcefulConfig) addTemplateAsset("common/src/main/java/$groupPath/config/${className}Config.java", "ModConfig.java", templates)
            if (resourcefulConfig && clientCode) addTemplateAsset("common/src/main/java/$groupPath/client/config/${className}ConfigClient.java", "ModConfigClient.java", templates)
            addTemplateAsset("common/build.gradle.kts", "Common build.gradle.kts", templates)
            addTemplateAsset("common/src/main/resources/${modid}-common.mixins.json", "Common mod-common.mixins.json", templates)

            // Fabric
            addTemplateAsset("fabric/src/main/java/$groupPath/fabric/${className}Fabric.java", "FabricMain.java", templates)
            if (clientCode) addTemplateAsset("fabric/src/main/java/$groupPath/client/fabric/${className}ClientFabric.java", "FabricModClient.java", templates)
            addTemplateAsset("fabric/gradle.properties", "Fabric gradle.properties", templates)
            addTemplateAsset("fabric/build.gradle.kts", "Fabric build.gradle.kts", templates)
            addTemplateAsset("fabric/src/main/resources/${modid}.mixins.json", "Fabric mod.mixins.json", templates)
            addTemplateAsset("fabric/src/main/resources/fabric.mod.json", "fabric.mod.json", templates)
            addTemplateAsset("fabric/run/options.txt", "options.txt", templates)
            addTemplateAsset("fabric/run/eula.txt", "eula.txt", templates)
            addTemplateAsset("fabric/run/server.properties", "server.properties", templates)

            // Neo Forge
            addTemplateAsset("neoforge/src/main/java/$groupPath/neoforge/${className}NeoForge.java", "NeoForgeMain.java", templates)
            if (clientCode) addTemplateAsset("neoforge/src/main/java/$groupPath/client/neoforge/${className}ClientNeoForge.java", "NeoForgeModClient.java", templates)
            addTemplateAsset("neoforge/gradle.properties", "NeoForge gradle.properties", templates)
            addTemplateAsset("neoforge/build.gradle.kts", "NeoForge build.gradle.kts", templates)
            addTemplateAsset("neoforge/src/main/resources/${modid}.mixins.json", "NeoForge mod.mixins.json", templates)
            addTemplateAsset("neoforge/src/main/resources/META-INF/mods.toml", "mods.toml", templates)
            if (datagen) addTemplateAsset("neoforge/src/main/java/$groupPath/datagen/${className}DataGenerator.java", "ModDataGenerator.java", templates)
            addTemplateAsset("neoforge/run/options.txt", "options.txt", templates)
            addTemplateAsset("neoforge/run/eula.txt", "eula.txt", templates)
            addTemplateAsset("neoforge/run/server.properties", "server.properties", templates)
        }
    }
}
