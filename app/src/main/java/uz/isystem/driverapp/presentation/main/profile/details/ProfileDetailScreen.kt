package uz.isystem.driverapp.presentation.main.profile.details


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.isystem.driverapp.R
import uz.isystem.driverapp.data.models.user.UserProfile
import uz.isystem.driverapp.databinding.ScreenProfileDetailBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class ProfileDetailScreen : BaseFragment(R.layout.screen_profile_detail) {
    private val binding by viewBinding(ScreenProfileDetailBinding::bind)
    private var dbr: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var auth: FirebaseAuth
    private lateinit var vel: ValueEventListener
    private var isLoading = false


    override fun onDestroyView() {
        super.onDestroyView()
        dbr?.removeEventListener(vel)
    }

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        uid = auth.uid.toString()
        dbr =
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile_details")
        setNotEditable()
        setData()
        setActions()
    }

    private fun setNotEditable() {
        binding.lastNameEdt.isEnabled = false
        binding.firstNameEdt.isEnabled = false
        binding.emailEdt.isEnabled = false
        binding.phoneEdt.isEnabled = false
        binding.cityEdt.isEnabled = false
        binding.emailEdt.isEnabled = false
        binding.saveBtn.visibility = View.GONE
    }

    private fun setData() {
        isLoading = true
        showLoading()
        vel = dbr!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserProfile::class.java).let {
                    binding.cityEdt.setText(it?.city)
                    binding.emailEdt.setText(it?.email)
                    binding.phoneEdt.setText(it?.phone)
                    binding.firstNameEdt.setText(it?.name)
                    binding.lastNameEdt.setText(it?.lastName)
                    isLoading = false
                    hideLoading()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Bad connection", Toast.LENGTH_SHORT).show()
                isLoading = false
                hideLoading()
            }
        })
    }


    private fun setActions() {
        binding.saveBtn.setOnClickListener {
            if (!isLoading) {
                save()
            }
        }
        binding.editBtn.setOnClickListener {
            setEditable()
        }
    }

    private fun save() {
        val firstName = binding.firstNameEdt.text.toString()
        val lastName = binding.lastNameEdt.text.toString()
        val email = binding.emailEdt.text.toString()
        val phone = binding.phoneEdt.text.toString()
        val city = binding.cityEdt.text.toString()

        val data = UserProfile(
            lastName = lastName,
            name = firstName,
            email = email,
            phone = phone,
            city = city
        )
        if (firstName.isBlank() || firstName.isEmpty()) {
            binding.firstNameEdt.error = "Input please"
            return
        }
        if (lastName.isBlank() || lastName.isEmpty()) {
            binding.lastNameEdt.error = "Input please"
            return
        }
        if (email.isBlank() || email.isEmpty()) {
            binding.emailEdt.error = "Input please"
            return
        }
        if (phone.isBlank() || phone.isEmpty()) {
            binding.phoneEdt.error = "Input please"
            return
        }
        if (city.isBlank() || city.isEmpty()) {
            binding.cityEdt.error = "Input please"
            return
        }
        isLoading = true
        showLoading()
        dbr!!.setValue(data).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                dbr!!.removeEventListener(vel)
                findNavController().navigate(
                    ProfileDetailScreenDirections.actionProfileDetailScreenToMainScreen(
                        true
                    )
                )
                isLoading = false
                hideLoading()
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                isLoading = false
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding.saveBtn.setTextColor(Color.TRANSPARENT)
        binding.saveBtn.alpha = 0.5f
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.saveBtn.setTextColor(Color.BLACK)
        binding.saveBtn.alpha = 1f
        binding.progressBar.visibility = View.GONE
    }

    private fun setEditable() {
        binding.firstNameEdt.isEnabled = true
        binding.lastNameEdt.isEnabled = true
        binding.phoneEdt.isEnabled = true
        binding.emailEdt.isEnabled = true
        binding.cityEdt.isEnabled = true
        binding.saveBtn.visibility = View.VISIBLE
    }
}