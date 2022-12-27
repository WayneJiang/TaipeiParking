package com.wayne.taipeiparking.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wayne.taipeiparking.R

class AlertDialogFragment : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(
            requireContext()
        ).apply {
            setTitle(getString(R.string.app_name))
            setMessage(requireArguments().getString("DIALOG_MESSAGE"))
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                findNavController().navigateUp()
            }
        }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}