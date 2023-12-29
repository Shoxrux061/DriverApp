package uz.isystem.driverapp.presentation.details


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import uz.isystem.driverapp.data.models.user.VehicleDetails
import uz.isystem.driverapp.databinding.ScreenVehicleDetailsBinding
import uz.isystem.driverapp.presentation.base.BaseFragment

class VehicleDetailsScreen : BaseFragment(R.layout.screen_vehicle_details) {
    private val binding by viewBinding(ScreenVehicleDetailsBinding::bind)
    private lateinit var dbr: DatabaseReference
    private lateinit var uid: String
    private lateinit var auth: FirebaseAuth
    private var isLoading = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        uid = auth.uid.toString()
        dbr =
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("vehicle_details")
        setNotEditable()
        setData()
        setActions()
        onBackPressed()
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(VehicleDetailsScreenDirections.actionVehicleDetailsScreenToMainScreen(true))
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
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

    private fun setEditable() {
        binding.brandEdt.isEnabled = true
        binding.modelEdt.isEnabled = true
        binding.numberEdt.isEnabled = true
        binding.yearEdt.isEnabled = true
        binding.saveBtn.visibility = View.VISIBLE
    }

    private fun setData() {
        dbr.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(VehicleDetails::class.java).let {
                    binding.brandEdt.setText(it?.brand)
                    binding.modelEdt.setText(it?.model)
                    binding.numberEdt.setText(it?.plateNum)
                    binding.yearEdt.setText(it?.year.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Bad connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setNotEditable() {
        binding.brandEdt.isEnabled = false
        binding.modelEdt.isEnabled = false
        binding.numberEdt.isEnabled = false
        binding.yearEdt.isEnabled = false
        binding.saveBtn.visibility = View.GONE
    }

    private fun save() {
        val brand = binding.brandEdt.text.toString()
        val model = binding.modelEdt.text.toString()
        val number = binding.numberEdt.text.toString()
        val year = binding.yearEdt.text.toString()

        val data = VehicleDetails(
            brand = brand,
            model = model,
            plateNum = number,
            year = year
        )

        if (brand.isBlank() || brand.isEmpty()) {
            binding.brandEdt.error = "Input please"
            return
        }
        if (model.isEmpty() || model.isBlank()) {
            binding.modelEdt.error = "Input please"
            return
        }
        if (number.isBlank() || number.isEmpty()) {
            binding.numberEdt.error = "Input please"
            return
        }
        if (year.isEmpty() || year.isBlank()) {
            binding.yearEdt.error = "Input please"
            return
        }

        showLoading()
        isLoading = true
        dbr.setValue(data).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                hideLoading()
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    VehicleDetailsScreenDirections.actionVehicleDetailsScreenToMainScreen(
                        true
                    )
                )
            } else {
                hideLoading()
                isLoading = false
            }
        }
    }

    private fun showLoading() {
        binding.saveBtn.alpha = 0.5f
        binding.progressBar.visibility = View.VISIBLE
        binding.saveBtn.setTextColor(Color.TRANSPARENT)
    }

    private fun hideLoading() {
        binding.saveBtn.alpha = 1f
        binding.progressBar.visibility = View.GONE
        binding.saveBtn.setTextColor(Color.BLACK)
    }
}
