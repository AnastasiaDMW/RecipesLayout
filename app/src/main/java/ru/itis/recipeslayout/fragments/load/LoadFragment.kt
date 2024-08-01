package ru.itis.recipeslayout.fragments.load

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.FragmentLoadBinding

class LoadFragment : Fragment(R.layout.fragment_load) {

    private var binding: FragmentLoadBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoadBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}