package com.bottlerocketstudios.brarchitecture.ui.projects

import com.bottlerocketstudios.brarchitecture.domain.repositories.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.BaseViewModel
import com.bottlerocketstudios.compose.projects.ProjectsItemState
import com.bottlerocketstudios.compose.util.asMutableState
import com.bottlerocketstudios.compose.util.formattedUpdateTime
import java.time.Clock
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class ProjectsViewModel : BaseViewModel() {
    // region DI
    private val repo: BitbucketRepository by inject()
    private val clock by inject<Clock>()
    // endregion

    // region UI State
    val projectsList =
            repo.projects.map {
                it.map { dto ->
                    ProjectsItemState(
                            name = dto.name.asMutableState(),
                            key = dto.key.asMutableState(),
                            updated =
                                    dto.updatedOn
                                            .formattedUpdateTime(clock)
                                            .getString()
                                            .asMutableState()
                    )
                }
            }
    // endregion

    // region Init
    init {
        launchIO { repo.getProjects() }
    }
    // endregion
}
