package com.dashmed.employee

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dashmed.employee.databinding.ActivityMainBinding
import com.dashmed.employee.fragments.Pending
import com.dashmed.employee.fragments.Settings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object { var reloadFragment = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        if (Utils.getUID(this) == null)
            startActivity(Intent(this, LoginActivity::class.java))

        binding.bottomNav.setOnItemSelectedListener {
            replaceFragment(it.itemId)
            true
        }

        binding.bottomNav.setOnItemReselectedListener { false }

        setContentView(binding.root)
    }

    override fun onResume() {
        if (Utils.getUID(this) == null)
            this.finish()

        if (reloadFragment) {
            if (binding.bottomNav.selectedItemId == R.id.nav_pending)
                replaceFragment(R.id.nav_pending)
            else
                binding.bottomNav.selectedItemId = R.id.nav_pending
        }

        super.onResume()
    }

    private fun replaceFragment(itemId: Int) {
        supportFragmentManager.beginTransaction().apply {
            when (itemId) {
                R.id.nav_pending -> {
                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    replace(R.id.main_container, Pending())
                }
                R.id.nav_settings -> {
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    replace(R.id.main_container, Settings())
                }
            }
        }.commit()
    }
}