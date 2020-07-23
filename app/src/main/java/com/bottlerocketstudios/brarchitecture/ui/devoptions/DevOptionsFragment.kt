package com.bottlerocketstudios.brarchitecture.ui.devoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
class DevOptionsFragment : BaseFragment() {

    private val vm: DevOptionsViewModel by viewModel()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return DataBindingUtil.inflate<DevOptionsFragmentBinding>(inflater, R.layout.dev_options_fragment, container, false).apply {
            viewModel = vm
            lifecycleOwner = this@DevOptionsFragment
            setupObservers(this)
        }.root
    }

    private fun setupObservers(binding: DevOptionsFragmentBinding) {
        vm.messageToUser.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
        vm.environmentDropdownDismissed.observe(viewLifecycleOwner, Observer {
            // Clear focus on the environment switcher to prevent the dropdown from showing unintentionally on orientation change (related to focus still being on the environment switcher)
            binding.environmentSwitcherInputLayout.clearFocus()
        })
    }
}
