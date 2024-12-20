package org.example.mytask.di

import org.example.mytask.data.modules.FirebaseModule
import org.example.mytask.data.repository.AuthRepositoryImpl
import org.example.mytask.data.repository.TaskRepositoryImpl
import org.example.mytask.domain.repository.AuthRepository
import org.example.mytask.domain.repository.TaskRepository
import org.example.mytask.domain.use_cases.AuthUseCase
import org.example.mytask.domain.use_cases.TaskUseCase
import org.example.mytask.presentation.screens.account.AccountViewModel
import org.example.mytask.presentation.screens.splash.SplashScreenViewModel
import org.example.mytask.presentation.screens.tasks.TaskViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appProvider = module {
    single { FirebaseModule }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }

    singleOf(::AuthUseCase) { bind<AuthUseCase>() }
    singleOf(::TaskUseCase) { bind<TaskUseCase>() }

    viewModelOf(::TaskViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::AccountViewModel)

}
