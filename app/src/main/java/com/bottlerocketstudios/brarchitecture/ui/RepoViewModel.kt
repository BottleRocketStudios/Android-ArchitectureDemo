package com.bottlerocketstudios.brarchitecture.ui

import android.app.Application
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository

abstract class RepoViewModel(app: Application, val repo: BitbucketRepository) : ScopedViewModel(app)
