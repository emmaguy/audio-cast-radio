package com.emmaguy.audiocastradio.di

import com.emmaguy.audiocastradio.base.BaseComponent
import kotlin.properties.Delegates

interface Injector : BaseComponent

internal class InjectorImpl(baseComponent: BaseComponent) : Injector, BaseComponent by baseComponent

object Inject {
    var instance: Injector by Delegates.notNull()
}