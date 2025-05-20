package com.geoiq.lk.annotations

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty

/**
 * A property that can be observed as a Flow.
 */
class FlowProperty<T>(initialValue: T) {
    private val _flow = MutableStateFlow(initialValue)
    
    /**
     * The current value of the property.
     */
    var value: T
        get() = _flow.value
        set(value) {
            _flow.value = value
        }
    
    /**
     * Get the property as a Flow.
     */
    fun asFlow(): Flow<T> = _flow
}

/**
 * Extension function to create a Flow from a FlowProperty.
 */
val <T> FlowProperty<T>.flow: Flow<T>
    get() = asFlow()
