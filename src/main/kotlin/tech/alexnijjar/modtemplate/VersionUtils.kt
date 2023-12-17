package tech.alexnijjar.modtemplate

import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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

    fun modrinth(slug: String, minecraftVersion: String): String {
        try {
            val baseUrl = "https://api.modrinth.com/v2/project/$slug/version"
            val queryParam = URLEncoder.encode("[\"$minecraftVersion\"]", StandardCharsets.UTF_8.toString())
            val url = URL("$baseUrl?game_versions=$queryParam")
            val reader = BufferedReader(InputStreamReader(url.openStream()))
            val response = reader.use { it.readText() }
            return JsonParser.parseString(response).asJsonArray[0].asJsonObject.getAsJsonPrimitive("version_number").asString
        } catch (e: Exception) {
            throw Exception("$slug does not have a $minecraftVersion version!")
        }
    }

    fun gradle(): String {
        val url = URL("https://services.gradle.org/versions/current")
        val reader = BufferedReader(InputStreamReader(url.openStream()))
        val response = reader.use { it.readText() }
        return JsonParser.parseString(response).asJsonObject.getAsJsonPrimitive("version").asString
    }

    fun parchment(minecraftVersion: String) = fetchVersion("https://ldtteam.jfrog.io/artifactory/parchmentmc-internal/org/parchmentmc/data/parchment-$minecraftVersion/maven-metadata.xml")

    fun archLoom() = fetchVersion("https://maven.architectury.dev/dev/architectury/architectury-loom/maven-metadata.xml")

    fun fabricLoader() = fetchVersion("https://maven.fabricmc.net/net/fabricmc/fabric-loader/maven-metadata.xml")

    fun neoForge() = fetchVersion("https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml")
}