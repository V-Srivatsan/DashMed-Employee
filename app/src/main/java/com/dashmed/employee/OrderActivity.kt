package com.dashmed.employee

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dashmed.employee.databinding.ActivityOrderBinding
import com.dashmed.employee.fragments.OrderDetails

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)

        setSupportActionBar(binding.orderToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_ind)
            title = null
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    replace(R.id.order_container, OrderDetails())
                }.commit()
            else
                this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}