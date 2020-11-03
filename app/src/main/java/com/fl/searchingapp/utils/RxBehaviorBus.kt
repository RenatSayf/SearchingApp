package com.fl.searchingapp.utils

import io.reactivex.subjects.BehaviorSubject


object RxBehaviorBus
{
    private val bus: BehaviorSubject<Any> = BehaviorSubject.create()

    fun send(any: Any)
    {
        bus.onNext(any)
    }

    fun toObservable() = bus
}