package com.nalepa.pizzeriaapplication

import android.content.Intent
import androidx.fragment.app.Fragment
import com.nalepa.pizzeriaapplication.activities.MainActivity
import com.nalepa.pizzeriaapplication.activities.RegistrationActivity

abstract class BaseFragment: Fragment() {

    fun startApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun finishApp() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("LOGOUT", true)
        startActivity(intent)

        requireActivity().finish()
    }

    fun logout() {
        val intent = Intent(requireContext(), RegistrationActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}