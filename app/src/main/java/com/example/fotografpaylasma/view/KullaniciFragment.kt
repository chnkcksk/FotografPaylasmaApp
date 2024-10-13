package com.example.fotografpaylasma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.fotografpaylasma.databinding.FragmentKullaniciBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class KullaniciFragment : Fragment() {

    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kayitButon.setOnClickListener { kayitOl(it) }
        binding.girisButton.setOnClickListener { girisYap(it) }

        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null) {
            //Kullanici daha onceden giris yapmis
            val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun kayitOl(view: View) {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEdittext.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Kullanici olusturuldu
                    Toast.makeText(
                        requireContext(),
                        "Kullanici basariyla olusturuldu.",
                        Toast.LENGTH_LONG
                    ).show()
                    val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Kullanici adi veya sifre bos birakilamaz.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun girisYap(view: View) {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEdittext.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                //Basarili sekilde giris yapilirsa
                Toast.makeText(
                    requireContext(),
                    "Basarili sekilde giris yapildi",
                    Toast.LENGTH_LONG
                )
                    .show()
                val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}