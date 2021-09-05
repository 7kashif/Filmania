package com.example.filmaxtesting

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

//        setCurrentFragment(HomeFragment())
//
//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.menuHome -> setCurrentFragment(HomeFragment())
//                R.id.menuSearch -> { Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_searchFragment) }
//                R.id.menuPeople -> { Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_popularPeopleFragment3) }
//                R.id.menuBookMarks -> { Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_bookMarksFragment) }
//            }
//            true
//        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        this.supportFragmentManager.beginTransaction().apply {
            replace(R.id.navHostFragment, fragment)
            commit()
        }
    }

//    override fun onBackPressed() {
//        val fragment: Fragment? = supportFragmentManager.findFragmentById(binding.flFragment.id)
//        if (fragment is HomeFragment)
//            finish()
//        else {
//            setCurrentFragment(HomeFragment())
//            binding.bottomNavigationView.selectedItemId = R.id.menuHome
//        }
//    }

}