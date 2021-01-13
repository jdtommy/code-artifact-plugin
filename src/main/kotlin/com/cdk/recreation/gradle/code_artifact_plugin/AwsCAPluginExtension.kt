package com.cdk.recreation.gradle.code_artifact_plugin

import java.lang.IllegalArgumentException

open class AwsCAPluginExtension {
    var url: String = ""
        get() {
            return if (field.isEmpty()) {
                throw IllegalArgumentException("Url cannot be empty")
//                field
            } else {
                field
            }
        }
    var domain: String = ""
        get() {
            return if (field.isEmpty()) {
                throw IllegalArgumentException("Domain cannot be empty")
//                field
            } else {
                field
            }
        }
    var domainOwner: String = ""
        get() {
            return if (field.isEmpty()) {
                throw IllegalArgumentException("Domain owner cannot be empty")
//                field
            } else {
                field
            }
        }
}