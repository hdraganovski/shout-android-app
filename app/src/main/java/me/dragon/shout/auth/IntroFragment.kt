package me.dragon.shout.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_intro.*
import me.dragon.shout.R

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        authIntroFragment_button_logIn.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_logInFragment)
        }

        authIntroFragment_button_register.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_registerFragment)
        }
    }
}
