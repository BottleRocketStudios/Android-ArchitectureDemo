package com.bottlerocketstudios.brarchitecture.ui.projects

import com.bottlerocketstudios.compose.projects.ProjectsItemState

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import java.time.Clock

class ProjectsViewModel : BaseViewModel() {

    // DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()

    val projectsList = repo.projects.map {
        it.map { dto ->
            ProjectsItemState(
                name = dto.name.asMutableState(),
                key = dto.key.asMutableState(),
                updated = dto.updatedOn.formattedUpdateTime(clock).getString().asMutableState()
            )
        }
    }

    // Init logic
    init {
        launchIO {
            getProjects()
        }
    }

    private fun getProjects() {
        launchIO {
            repo.getProjects()
        }
    }
}
