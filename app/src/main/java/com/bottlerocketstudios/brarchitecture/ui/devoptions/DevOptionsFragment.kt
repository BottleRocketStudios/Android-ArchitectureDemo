package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseFragment
import com.bottlerocketstudios.brarchitecture.data.buildconfig.BuildConfigProvider
import com.bottlerocketstudios.brarchitecture.databinding.DevOptionsFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Screen that manages dev options (only accessible from internal or debug builds (aka non release production builds)
 */
class DevOptionsFragment : BaseFragment<DevOptionsViewModel, DevOptionsFragmentBinding>() {
    override val fragmentViewModel: DevOptionsViewModel by viewModel()
    private val buildConfigProvider by inject<BuildConfigProvider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Only debug/internal builds allowed to show this screen. Immediately close if somehow launched on prod release build.
        if (buildConfigProvider.isProductionReleaseBuild) {
            // NOTE: Special case usage of findNavController
            findNavController().popBackStack()
            return
        }
    }

    override fun getLayoutRes(): Int = R.layout.dev_options_fragment

    override fun setupBinding(binding: DevOptionsFragmentBinding) {
        super.setupBinding(binding)
        fragmentViewModel.messageToUser.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
        fragmentViewModel.environmentDropdownDismissed.observe(viewLifecycleOwner, Observer {
            // Clear focus on the environment switcher to prevent the dropdown from showing unintentionally on orientation change (related to focus still being on the environment switcher)
            binding.environmentSwitcherInputLayout.clearFocus()
        })
    }
}
