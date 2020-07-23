package com.bottlerocketstudios.brarchitecture.ui.user

import android.app.Application
import com.bottlerocketstudios.brarchitecture.data.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel

class UserFragmentViewModel(app: Application, val repo: BitbucketRepository) : BaseViewModel(app)
