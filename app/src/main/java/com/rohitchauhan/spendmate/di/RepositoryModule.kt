package com.rohitchauhan.spendmate.di

import com.rohitchauhan.spendmate.data.repoimpl.RepoImpl
import com.rohitchauhan.spendmate.domain.repo.Repo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideRepo(repoImpl: RepoImpl): Repo
}