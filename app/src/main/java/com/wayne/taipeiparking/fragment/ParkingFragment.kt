package com.wayne.taipeiparking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wayne.taipeiparking.AppViewModel
import com.wayne.taipeiparking.ParkingLotAdapter
import com.wayne.taipeiparking.R
import com.wayne.taipeiparking.databinding.FragmentParkingBinding
import com.wayne.taipeiparking.webservice.WebService

class ParkingFragment : Fragment() {
    private lateinit var mFragmentParkingBinding: FragmentParkingBinding

    private val mParkingLotAdapter: ParkingLotAdapter by lazy {
        ParkingLotAdapter()
    }

    private val mAppViewModel: AppViewModel by lazy {
        ViewModelProvider(this)[AppViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentParkingBinding = FragmentParkingBinding.inflate(inflater, container, false)
        return mFragmentParkingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().navigate(R.id.action_to_loading)

        requestData()

        mAppViewModel.queryAllParkingInfo().observe(viewLifecycleOwner) {
            mParkingLotAdapter.submitList(it)
        }

        mFragmentParkingBinding.apply {
            rv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mParkingLotAdapter
            }

            btnSync.setOnClickListener {
                findNavController().navigate(R.id.action_to_loading)

                requestData()
            }

            btnAccount.setOnClickListener {
                findNavController().navigate(R.id.action_to_profile)
            }
        }
    }

    private fun requestData() =
        WebService.requestDownloadParkingJSON(requireContext()) { success ->
            findNavController().navigateUp()

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