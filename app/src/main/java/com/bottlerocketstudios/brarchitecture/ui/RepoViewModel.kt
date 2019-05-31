package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


open class RepoViewModel(app: Application, val repo: BitbucketRepository) : ScopedViewModel(app) {
    
}