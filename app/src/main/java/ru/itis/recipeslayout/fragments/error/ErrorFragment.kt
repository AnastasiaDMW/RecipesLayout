package ru.itis.recipeslayout.fragments.error

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.FragmentErrorBinding

class ErrorFragment : Fragment(R.layout.fragment_error) {

    private var binding: FragmentErrorBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentErrorBinding.bind(view)
    }
}