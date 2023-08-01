package com.nik.tkforum.ui.login

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.R
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.databinding.FragmentLoginBinding
import com.nik.tkforum.ui.BaseFragment
import com.nik.tkforum.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    override val binding get() = _binding as FragmentLoginBinding
    override val layoutId: Int get() = R.layout.fragment_login

    private lateinit var signInClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferenceManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityResultLauncher()
        auth = FirebaseAuth.getInstance()
        signInClient = Identity.getSignInClient(requireActivity())
        setSingInRequest()

        binding.btGoogleSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun setSingInRequest() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun signIn() {
        signInClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    activityResultLauncher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                    viewModel.getSevenCharacterList()
                    viewModel.getEightCharacterList()
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "sign-in error: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                Log.d(TAG, "sign-in fail: ${e.localizedMessage}")
                legacySignIn()
            }
            .addOnCanceledListener {
                Log.e(tag, "sign-in cancelled")
            }
    }

    private fun setActivityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val credential = signInClient.getSignInCredentialFromIntent(result.data)
                        val idToken = credential.googleIdToken
                        if (idToken != null) {
                            firebaseAuthWithGoogle(idToken)
                        } else {
                            Log.d(TAG, "No ID token")
                        }
                    } catch (e: ApiException) {
                        legacySignIn()
                    }
                }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val action = SignInFragmentDirections.actionNavLoginToNavHome()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(action)
                    saveUserInfo()
                    Log.d(TAG, "login")
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "${task.exception?.message}")
                }
            }
    }

    private fun legacySignIn() {
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .build()
        signInClient
            .getSignInIntent(request)
            .addOnSuccessListener { result ->
                try {
                    activityResultLauncher.launch(
                        IntentSenderRequest.Builder(result.intentSender).build()
                    )
                    viewModel.getSevenCharacterList()
                    viewModel.getEightCharacterList()
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Google Sign-in failed")
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "legacySignIn: ", e)
            }
    }

    private fun saveUserInfo() {
        val firebase = auth.currentUser
        with(preferencesManager) {
            putString(Constants.KEY_NICKNAME, firebase?.displayName.toString())
            putString(Constants.KEY_MAIL_ADDRESS, firebase?.email.toString())
            putString(Constants.KEY_PROFILE_IMAGE, firebase?.photoUrl.toString())
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}