package uz.isystem.driverapp.presentation.main.profile

import android.os.Bundle
import android.view.View
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
import uz.isystem.driverapp.databinding.PageProfileBinding
import uz.isystem.driverapp.presentation.base.BaseFragment
import uz.isystem.driverapp.presentation.main.MainScreenDirections

class ProfilePage : BaseFragment(R.layout.page_profile) {
    private val binding by viewBinding(PageProfileBinding::bind)
    private lateinit var dbr: DatabaseReference
    private lateinit var uid: String
    private var vel: ValueEventListener? = null
    override fun onCreate(view: View, savedInstanceState: Bundle?) {



        uid = FirebaseAuth.getInstance().uid.toString()

        dbr =
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile_details")

        vel = dbr.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserProfile::class.java).let {
                    val userName = "${it?.name?:"User"} ${it?.lastName?:""}"
                    binding.emailAdress.text = it?.email
                    if(userName.isNotEmpty()) {
                        binding.userName.text = userName
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.profileSetting.setOnClickListener {
            findNavController().navigate(MainScreenDirections.actionMainScreenToProfileDetailScreen())
        }
        binding.carDetails.setOnClickListener {
            findNavController().navigate(MainScreenDirections.actionMainScreenToVehicleDetailsScreen())
        }
    }
}
