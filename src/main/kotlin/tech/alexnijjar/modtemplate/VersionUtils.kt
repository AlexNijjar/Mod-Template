package tech.alexnijjar.modtemplate

import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

object VersionUtils {

    private fun fetchVersion(xml: String): String {
        val reader = BufferedReader(InputStreamReader(URL(xml).openStream()))
        val response = reader.use { it.readText() }

        return DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(response.byteInputStream())
                .getElementsByTagName("versioning")
                .item(0).childNodes
                .item(1).textContent
    }

    fun getLatestGradleVersion(): String {
        val url = URL("https://services.gradle.org/versions/current")
        val reader = BufferedReader(InputStreamReader(url.openStream()))
        val response = reader.use { it.readText() }
        return JsonParser.parseString(response).asJsonObject.getAsJsonPrimitive("version").asString
    }

    fun getLatestParchmentVersion() = fetchVersion("https://ldtteam.jfrog.io/artifactory/parchmentmc-internal/org/parchmentmc/data/parchment-1.20.1/maven-metadata.xml")
    fun getArchLoomVersion() = fetchVersion("https://maven.architectury.dev/dev/architectury/architectury-loom/maven-metadata.xml")
    fun getModMenuVersion() = fetchVersion("https://maven.terraformersmc.com/releases/com/terraformersmc/modmenu/maven-metadata.xml")
    fun getCommonAtsVersion() = fetchVersion("https://maven.resourcefulbees.com/repository/thatgravyboat/tech/thatgravyboat/commonats/maven-metadata.xml")
    fun getFabricLoaderVersion() = fetchVersion("https://maven.fabricmc.net/net/fabricmc/fabric-loader/maven-metadata.xml")
    fun getFabricApiVersion() = fetchVersion("https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml").split("+")[0]
    fun getResourcefulLibVersion() = fetchVersion("https://maven.resourcefulbees.com/repository/maven-releases/com/teamresourceful/resourcefullib/resourcefullib-common-1.20.1/maven-metadata.xml")
    fun getResourcefulConfigVersion() = fetchVersion("https://maven.resourcefulbees.com/repository/maven-releases/com/teamresourceful/resourcefulconfig/resourcefulconfig-common-1.20/maven-metadata.xml")
    fun getBotariumVersion() = fetchVersion("https://maven.resourcefulbees.com/repository/codexadrian/earth/terrarium/botarium-common-1.20.1/maven-metadata.xml")
    fun getReiVersion() = fetchVersion("https://maven.shedaniel.me/me/shedaniel/RoughlyEnoughItems-api/maven-metadata.xml")
    fun getJeiVersion() = fetchVersion("https://maven.blamejared.com/mezz/jei/jei-1.20.1-lib/maven-metadata.xml")
    fun getGeckoLibVersion() = fetchVersion("https://maven.resourcefulbees.com/repository/geckolib/software/bernie/geckolib/geckolib-forge-1.20.1/maven-metadata.xml")
}