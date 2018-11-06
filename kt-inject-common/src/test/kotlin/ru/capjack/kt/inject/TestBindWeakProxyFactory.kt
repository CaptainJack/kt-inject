package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertNotEquals

class TestBindWeakProxyFactory {
	@Test
	fun typed() {
		val injector = injector {
			bindProxyFactorySupplier<StubUserFactory> {
				delegate<StubUser, StubUserImpl>()
			}
		}
		
		val a = injector.get<StubUserFactory>()
		val b = injector.get<StubUserFactory>()
		
		assertNotEquals(a, b)
	}
	
	@Test
	fun named() {
		val name = stubNameFactory
		
		val injector = injector {
			bindProxyFactorySupplier(name) {
				delegate<StubUser, StubUserImpl>()
			}
		}
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertNotEquals(a, b)
	}
}