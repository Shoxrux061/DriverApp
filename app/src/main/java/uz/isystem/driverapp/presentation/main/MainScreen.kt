package uz.isystem.driverapp.presentation.main

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
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
import uz.isystem.driverapp.data.cache.AppCache
import uz.isystem.driverapp.data.models.user.UserProfile
import uz.isystem.driverapp.databinding.CustomDialogBinding
import uz.isystem.driverapp.databinding.HeaderBinding
import uz.isystem.driverapp.databinding.ScreenMainBinding
import uz.isystem.driverapp.presentation.adapter.MainAdapter
import uz.isystem.driverapp.presentation.base.BaseFragment

class MainScreen : BaseFragment(R.layout.screen_main) {

    private val binding by viewBinding(ScreenMainBinding::bind)
    private lateinit var dialog: Dialog
    private lateinit var dialogBinding: CustomDialogBinding
    private lateinit var headerBinding: HeaderBinding
    private var dbr: DatabaseReference? = null
    private lateinit var uid: String
    private var vel: ValueEventListener? = null
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        uid = FirebaseAuth.getInstance().uid.toString()
        dbr = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profile_details")
        headerBinding = HeaderBinding.bind(binding.drawer.getHeaderView(0))
        binding.viewPager.isUserInputEnabled = false
        onBackPressed()
        setProfileData()
        setAdapter()
        setDialog()
        setActions()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentItem = binding.viewPager.currentItem
                if (currentItem > 0) {
                    binding.viewPager.currentItem = 0
                } else {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                        return
                    }

                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
        })
    }

    private fun setAdapter() {
        val adapter = MainAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }

    private fun setProfileData() {
        vel = dbr!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserProfile::class.java).let {
                    val userName = "${it?.name?:"User"} ${it?.lastName?:""}"
                    headerBinding.profileName.text = userName
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setActions() {

        dialogBinding.yesBtn.setOnClickListener {
            AppCache.getObject().isNotSigned()
            AppCache.getObject().savePassword("")
            AppCache.getObject().saveEmail("")
            findNavController().navigate(MainScreenDirections.actionMainScreenToRegisterScreen())
            dialog.dismiss()
        }
        dialogBinding.noBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.drawerBtn.setOnClickListener {
            binding.drawerLaout.open()
        }
        binding.drawer.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.homeID -> {
                    binding.drawerLaout.close()
                    binding.viewPager.currentItem = 0
                }

                R.id.rideID -> {
                    binding.drawerLaout.close()
                    binding.viewPager.currentItem = 1
                }

                R.id.profileID -> {
                    binding.drawerLaout.close()
                    binding.viewPager.currentItem = 2
                }

                R.id.logoutID -> {
                    binding.drawerLaout.close()
                    dialog.show()
                }
            }
            true
        }
    }

    private fun setDialog() {
        dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT - 1, 450)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}