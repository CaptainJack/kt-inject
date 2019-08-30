package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Injector
import ru.capjack.tool.depin.TypedName
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

internal class Registry {
	private val classBindings = createConcurrentMutableMap<KClass<*>, Binding<*>>()
	private val nameBindings = createConcurrentMutableMap<TypedName<*>, Binding<*>>()
	private val smartClassProducers = createConcurrentMutableCollection<Injector.(KClass<*>) -> Any?>()
	private val smartParameterProducers = createConcurrentMutableCollection<Injector.(KParameter) -> Any?>()
	
	fun hasBinding(clazz: KClass<*>): Boolean {
		return classBindings.containsKey(clazz)
	}
	
	fun hasBinding(name: TypedName<*>): Boolean {
		return nameBindings.containsKey(name)
	}
	
	fun setBinding(clazz: KClass<*>, binding: Binding<*>) {
		classBindings[clazz] = binding
	}
	
	fun setBinding(name: TypedName<*>, binding: Binding<*>) {
		nameBindings[name] = binding
	}
	
	fun addSmartProducerForClass(producer: Injector.(KClass<*>) -> Any?) {
		smartClassProducers.add(producer)
	}
	
	fun addSmartProducerForParameter(producer: Injector.(KParameter) -> Any?) {
		smartParameterProducers.add(producer)
	}
	
	fun <T : Any> getBinding(clazz: KClass<T>): Binding<T>? {
		@Suppress("UNCHECKED_CAST")
		return classBindings[clazz] as Binding<T>?
	}
	
	fun <T : Any> getBinding(name: TypedName<T>): Binding<T>? {
		@Suppress("UNCHECKED_CAST")
		return nameBindings[name] as Binding<T>?
	}
	
	fun trySmartProduce(injector: Injector, clazz: KClass<*>): Any? {
		for (producer in smartClassProducers) {
			producer(injector, clazz)?.let {
				return it
			}
		}
		return null
	}
	
	fun trySmartProduce(injector: Injector, parameter: KParameter): Any? {
		for (producer in smartParameterProducers) {
			producer(injector, parameter)?.let {
				return it
			}
		}
		return null
	}
}