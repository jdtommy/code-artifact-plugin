package com.cdk.recreation.gradle.code_artifact_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.net.URI
import java.util.concurrent.TimeUnit

class AwsCAPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("AWSCodeArtifact", AwsCAPluginExtension::class.java)

        project.afterEvaluate {
            println("URL = ${extension.url}")

            val url = extension.url
            val domain = extension.domain
            val domainOwner = extension.domainOwner

            it.repositories.maven { repo ->
                repo.url = URI(url)
                repo.credentials { creds ->
                    creds.username = "AWS"
                    creds.password = getPassword(domain, domainOwner)
                }
            }
        }
    }

    private fun getPassword(domain: String, domainOwner: String): String {
        println("domain = ${domain}")
        println("domainOwner = ${domainOwner}")

        val builder = ProcessBuilder().command(
            "aws",
            "codeartifact",
            "get-authorization-token",
            "--domain",
            domain,
            "--domain-owner",
            domainOwner,
            "--query",
            "authorizationToken",
            "--output",
            "text",
        )

        val proc = builder.start()

        val time = 15L
        val timeUnit = TimeUnit.SECONDS

        val finished = proc.waitFor(time, timeUnit)

        if (!finished) {
            throw IllegalStateException("Login process did not finished with in $time-$timeUnit")
        }

        if (proc.exitValue() != 0) {
            val error = proc.errorStream.bufferedReader().readText()

            throw IllegalStateException("Could not log into AWS CodeArtifact: ${proc.exitValue()} \n $error")
        }

        return proc.inputStream.bufferedReader().readText()
    }
}