package uz.isystem.driverapp.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.isystem.driverapp.R
import uz.isystem.driverapp.data.cache.AppCache
import uz.isystem.driverapp.data.models.user.UserModel
import uz.isystem.driverapp.databinding.ScreenRegisterBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class RegisterScreen : BaseFragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private lateinit var auth: FirebaseAuth
    private lateinit var dbr: DatabaseReference
    private var isLoading = false
    private var cache = AppCache.getObject()
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        dbr = FirebaseDatabase.getInstance().getReference("users")

        setActions()
    }

    private fun setActions() {
        binding.createAccBtn.setOnClickListener {
            if (!isLoading) {
                createAcc()
            }
        }
        binding.haveAccBtn.setOnClickListener {
            findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToLoginScreen())
        }
    }

    private fun createAcc() {
        val email = binding.emailEdt.text.toString()
        val password = binding.passwordEdt.text.toString()
        val phone = binding.phoneEdt.text.toString()

        if (email.isEmpty() || email.isBlank()) {
            binding.emailEdt.error = "Input please"
            return
        }
        if (password.length < 8 || password.isBlank() || password.isEmpty()) {
            binding.passwordEdt.error = "Password must be at least 8 digits long"
            return
        }
        if (phone.isBlank() || phone.isEmpty()) {
            binding.phoneEdt.error = "Input please"
            return
        }
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
        binding.createAccBtn.alpha = 0.5f
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val uid = auth.uid.toString()
                val user = UserModel(uid = uid, password = password)
                dbr.child(uid).setValue(user)
                binding.progressBar.visibility = View.GONE
                binding.createAccBtn.alpha = 1f
                isLoading = false
                saveCache(password = password, email = email)
                findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToMainScreen())
            } else {
                binding.progressBar.visibility = View.GONE
                binding.createAccBtn.alpha = 1f
                isLoading = false
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCache(password: String, email: String) {
        cache.isSigned()
        cache.savePassword(password)
        cache.saveEmail(email)
    }
}
