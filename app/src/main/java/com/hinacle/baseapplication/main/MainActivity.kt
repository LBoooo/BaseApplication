package com.hinacle.baseapplication.main

import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.drake.statusbar.darkMode
import com.hinacle.base.app.AppActivity
import com.hinacle.base.binding.bindPager
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityAppMainBinding
import com.hinacle.baseapplication.simple.LoginDialog
import com.hinacle.baseapplication.simple.LoginDialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppActivity(R.layout.activity_app_main) {
    private val viewBinding by viewBinding(ActivityAppMainBinding::bind)

    override fun initView() {
        super.initView()
//        immersive(darkMode = true)
        darkMode()
        setupSmoothBottomMenu()
    }

    private fun setupSmoothBottomMenu() {

        //添加自定义的FixFragmentNavigator
//       val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        val fragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val fragmentNavigator =
//            FixFragmentNavigator(this, supportFragmentManager, fragment.id)
//        navController.navigatorProvider.addNavigator(fragmentNavigator)
//
//        navController.setGraph(R.navigation.nav_graph)


//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        navController.enableOnBackPressed(true)
//        navOptions {
//
//        }

//        viewBinding.bottomNaviBar.setupWithNavController(navController)

//        val popupMenu = PopupMenu(this, null)
//        popupMenu.inflate(R.menu.bottom_nav_menu)
//        val menu = popupMenu.menu
//        viewBinding.bottomBar.setupWithNavController(menu, navController)

        initViewPager(viewBinding.navViewPager)

        viewBinding.bottomBar.setOnItemSelectedListener {
            viewBinding.navViewPager.setCurrentItem(it, false)
        }

        LoginDialogHelper.loginDialog = LoginDialog.builder(supportFragmentManager).also { it.init() }

    }

    private fun initViewPager(vp: ViewPager2) {
        //是否可滑动
        vp.isUserInputEnabled = false
        vp.bindPager(
            supportFragmentManager,
            lifecycle,
            HomeFragment.newInstance(),
            RecommendFragment.newInstance(),
            MessageFragment.newInstance(),
            MineFragment.newInstance()
        )
    }
}