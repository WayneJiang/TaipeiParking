package com.wayne.taipeiparking.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wayne.taipeiparking.R
import com.wayne.taipeiparking.databinding.FragmentLoginBinding
import com.wayne.taipeiparking.webservice.WebService

class LoginFragment : Fragment() {
    private lateinit var mFragmentLoginBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return mFragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentLoginBinding.apply {
            textInputEditTextEmail.doOnTextChanged { text, _, _, count ->
                textInputLayoutEmail.error =
                    if ((count > 0) and
                        (!Patterns.EMAIL_ADDRESS.matcher(text?.trim().toString()).matches())
                    ) {
                        getString(R.string.email_format_invalid)
                    } else {
                        null
                    }
            }

            textInputEditTextPassword.doOnTextChanged { _, _, _, count ->
                if (count > 0) {
                    textInputLayoutPassword.error = null
                }
            }

            btnLogin.setOnClickListener {
                when {
                    textInputEditTextEmail.text.toString().trim().isNotEmpty() and
                            textInputEditTextPassword.text.toString().trim().isNotEmpty() -> {

                        findNavController().navigate(R.id.action_to_loading)

                        WebService.requestLogin(
                            textInputEditTextEmail.text.toString().trim(),
                            textInputEditTextPassword.text.toString().trim()
                        ) { success ->

                            findNavController().navigateUp()
                            if (success) {
                                findNavController().navigate(R.id.action_to_parking)
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
                    textInputEditTextPassword.text.toString().trim().isEmpty() -> {
                        textInputLayoutPassword.error = " "
                    }
                }
            }
        }
    }
}