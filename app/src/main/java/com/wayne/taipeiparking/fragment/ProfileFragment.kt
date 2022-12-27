package com.wayne.taipeiparking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wayne.taipeiparking.R
import com.wayne.taipeiparking.databinding.FragmentProfileBinding
import com.wayne.taipeiparking.repository.Repository
import com.wayne.taipeiparking.webservice.WebService

class ProfileFragment : Fragment() {
    private lateinit var mFragmentProfileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mFragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentProfileBinding.apply {
            btnBack.setOnClickListener {

                findNavController().navigate(R.id.action_to_loading)

                WebService.requestUserUpdate(ProfilePreferenceFragment.updateTimezone) { success ->
                    findNavController().apply {
                        navigateUp()
                        navigateUp()
                    }

                    if (!success) {
                        findNavController().navigate(
                            R.id.action_to_alert,
                            bundleOf(
                                "DIALOG_MESSAGE" to getString(R.string.error)
                            )
                        )
                    }
                }
            }

            btnLogout.setOnClickListener {
                findNavController().navigate(R.id.action_to_loading)

                Repository.clearAllTablesAndFile(requireContext()) { success ->
                    findNavController().navigateUp()

                    if (success) {
                        findNavController().navigate(R.id.action_to_login)
                    } else {
                        findNavController().navigate(
                            R.id.action_to_alert,
                            bundleOf(
                                "DIALOG_MESSAGE" to getString(R.string.error)
                            )
                        )
                    }
                }
            }
        }
    }
}