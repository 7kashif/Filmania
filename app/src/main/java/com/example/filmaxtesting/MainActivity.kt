package com.example.filmaxtesting

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.filmaxtesting.databinding.ActivityMainBinding
import com.example.filmaxtesting.fragments.BookMarksFragment
import com.example.filmaxtesting.fragments.HomeFragment
import com.example.filmaxtesting.fragments.SearchFragment
import com.example.filmaxtesting.fragments.people.PopularPeopleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> setCurrentFragment(HomeFragment())
                R.id.menuSearch -> { setCurrentFragment(SearchFragment()) }
                R.id.menuPeople -> { setCurrentFragment(PopularPeopleFragment()) }
                R.id.menuBookMarks -> { setCurrentFragment(BookMarksFragment()) }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment : Fragment)  {
        this.supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        val fragment : Fragment? = supportFragmentManager.findFragmentById(binding.flFragment.id)
        if(fragment is HomeFragment)
            finish()
        else {
            setCurrentFragment(HomeFragment())
            binding.bottomNavigationView.selectedItemId = R.id.menuHome
        }
    }

}