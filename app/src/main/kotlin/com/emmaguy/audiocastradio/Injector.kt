package com.emmaguy.audiocastradio

import kotlin.properties.Delegates

interface Injector : AppComponent

internal class InjectorImpl(appComponent: AppComponent) : Injector, AppComponent by appComponent

object Inject {
    var instance: Injector by Delegates.notNull()
}