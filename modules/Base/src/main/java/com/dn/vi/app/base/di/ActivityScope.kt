package com.dn.vi.app.base.di

import javax.inject.Scope

/**
 * Dagger scope for global App
 * Created by holmes on 2020/5/21.
 **/
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

/**
 * Dagger scope for activity
 * Created by holmes on 2020/5/21.
 **/
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope


/**
 * Dagger scope for Fragment(or View segment)
 * Created by holmes on 2020/5/21.
 **/
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewScope