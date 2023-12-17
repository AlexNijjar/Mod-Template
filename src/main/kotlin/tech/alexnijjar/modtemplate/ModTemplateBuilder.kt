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

            val gradleVersion = VersionUtils.getLatestGradleVersion()
            val parchmentVersion = VersionUtils.getLatestParchmentVersion()
            val archLoomVersion = VersionUtils.getArchLoomVersion()
            val modMenuVersion = VersionUtils.modrinth("modmenu")
            val commonAtsVersion = VersionUtils.getCommonAtsVersion()
            val fabricLoaderVersion = VersionUtils.getFabricLoaderVersion()
            val fabricApiVersion = VersionUtils.modrinth("fabric-api").split("+")[0]
            val forgeVersion = "47.1.3" // Cap at 47.1.3 because Lex is a BITCH

            val resourcefulLibVersion = VersionUtils.modrinth("resourceful-lib")
            val resourcefulConfigVersion = VersionUtils.modrinth("resourceful-config")
            val botariumVersion = VersionUtils.modrinth("botarium")
            val reiVersion = VersionUtils.modrinth("rei").split("+")[0]
            val jeiVersion = VersionUtils.modrinth("jei")
            val geckolibVersion = VersionUtils.modrinth("geckolib")

            val templates = mapOf(
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
                    "FORGE_VERSION" to forgeVersion,

                    "RESOURCEFUL_LIB_VERSION" to resourcefulLibVersion,
                    "RESOURCEFUL_CONFIG_VERSION" to resourcefulConfigVersion,
                    "BOTARIUM_VERSION" to botariumVersion,
                    "REI_VERSION" to reiVersion,
                    "JEI_VERSION" to jeiVersion,
                    "GECKOLIB_VERSION" to geckolibVersion,
            )

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
            addTemplateAsset("forge/src/main/java/$groupPath/forge/${className}Forge.java", "ForgeMain.java", templates)
            if (clientCode) addTemplateAsset("forge/src/main/java/$groupPath/client/forge/${className}ClientForge.java", "ForgeModClient.java", templates)
            addTemplateAsset("forge/gradle.properties", "Forge gradle.properties", templates)
            addTemplateAsset("forge/build.gradle.kts", "Forge build.gradle.kts", templates)
            addTemplateAsset("forge/src/main/resources/${modid}.mixins.json", "Forge mod.mixins.json", templates)
            addTemplateAsset("forge/src/main/resources/META-INF/mods.toml", "mods.toml", templates)
            addTemplateAsset("forge/src/main/resources/pack.mcmeta", "pack.mcmeta", templates)
            if (datagen) addTemplateAsset("forge/src/main/java/$groupPath/datagen/${className}DataGenerator.java", "ModDataGenerator.java", templates)
            addTemplateAsset("forge/run/options.txt", "options.txt", templates)
            addTemplateAsset("forge/run/eula.txt", "eula.txt", templates)
            addTemplateAsset("forge/run/server.properties", "server.properties", templates)
        }
    }
}
