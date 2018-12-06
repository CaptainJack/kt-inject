package ru.capjack.kt.inject.js.gradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import ru.capjack.kt.reflect.js.gradle.ReflectExtension
import ru.capjack.kt.reflect.js.gradle.ReflectPlugin
import ru.capjack.kt.reflect.js.gradle.ReflectTarget.Unit.ANNOTATIONS
import ru.capjack.kt.reflect.js.gradle.ReflectTarget.Unit.MEMBERS

open class InjectPlugin : Plugin<Project> {
	companion object {
		val VERSION = this::class.java.classLoader.getResource("kt-inject-version").readText()
	}
	
	override fun apply(project: Project) {
		configureDefaultVersionsResolutionStrategy(project)
		
		project.pluginManager.apply(ReflectPlugin::class)
		
		project.configure<ReflectExtension> {
			withAnnotation("ru.capjack.kt.inject.Inject")
			withAnnotation("ru.capjack.kt.inject.InjectBind", ANNOTATIONS)
			withAnnotation("ru.capjack.kt.inject.InjectProxy", ANNOTATIONS, MEMBERS)
			withAnnotation("ru.capjack.kt.inject.InjectImplementation", ANNOTATIONS)
		}
	}
	
	private fun configureDefaultVersionsResolutionStrategy(project: Project) {
		project.configurations.forEach { configuration ->
			configuration.resolutionStrategy.eachDependency(Action {
				if (requested.group == Const.GROUP && requested.name == Const.ARTIFACT_LIB && requested.version.isNullOrEmpty()) {
					useVersion(VERSION)
				}
			})
		}
	}
}