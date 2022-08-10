package com.hinacle.baseapplication

import com.hinacle.baseapplication.simple.NetModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InjectModel {
    @Provides fun provideNetModel() = NetModel()
}