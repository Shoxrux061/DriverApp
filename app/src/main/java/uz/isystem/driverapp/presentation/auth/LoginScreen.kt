package uz.isystem.driverapp.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import uz.isystem.driverapp.R
import uz.isystem.driverapp.data.cache.AppCache
import uz.isystem.driverapp.databinding.ScreenLoginBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class LoginScreen : BaseFragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var isLoading = false
    private val cache = AppCache.getObject()
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        setActions()
    }

    private fun setActions() {
        binding.loginBtn.setOnClickListener {
            if (!isLoading) {
                loginToAcc()
            }
        }
        binding.register.setOnClickListener {
            findNavController().navigate(LoginScreenDirections.actionLoginScreenToRegisterScreen())
        }
    }

    private fun loginToAcc() {
        val password = binding.passwordEdt.text.toString()
        val email = binding.emailEdt.text.toString()

        if (email.isEmpty() || email.isBlank()) {
            binding.emailEdt.error = "Input pleas"
            return
        }
        if (password.isBlank() || password.isEmpty()) {
            binding.passwordEdt.error = "Input pleas"
            return
        }
        isLoading = true
        binding.loginBtn.isClickable = false
        binding.progressBar.visibility = View.VISIBLE
        binding.loginBtn.alpha = 0.5f
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                binding.loginBtn.isClickable = true
                binding.loginBtn.alpha = 1f
                isLoading = false
                saveCache(email = email, password = password)
                findNavController().navigate(LoginScreenDirections.actionLoginScreenToMainScreen())
            } else {
                binding.progressBar.visibility = View.GONE
                binding.loginBtn.isClickable = true
                binding.loginBtn.alpha = 1f
                isLoading = false
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCache(email: String, password: String) {
        cache.isSigned()
        cache.saveEmail(email)
        cache.savePassword(password)
    }
}