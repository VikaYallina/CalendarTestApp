package com.yallina.myapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.yallina.myapplication.R

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
//
//        val db = Room.databaseBuilder(
//            this,
//            AppDatabase::class.java,
//            "tasks"
//        ).fallbackToDestructiveMigration()
//            .build()
//
////        fillDb(db)
//
//        GlobalScope.launch {
//             db.taskDao().selectTasksBetweenDates(
//                 LocalDateTime.of(2022,8,27,0,0,0),
//                 LocalDateTime.of(2022,8,27,11,59,59)
//             ).collect { tasks ->
//                 tasks.forEach { t -> Log.i(TAG, t.toString())}
//             }
//        }

    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    fun fillDb(db: AppDatabase){
//        GlobalScope.launch {
//            val tasks = listOf(
//                TaskEntity(
//                    name = "first",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now(),
//                    dateEnd = LocalDateTime.now().plusHours(2)
//                ),
//                TaskEntity(
//                    name = "seconod",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now(),
//                    dateEnd = LocalDateTime.now().plusHours(2)
//                ),
//                TaskEntity(
//                    name = "third",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now().minusHours(5),
//                    dateEnd = LocalDateTime.now().plusHours(2)
//                ),
//                TaskEntity(
//                    name = "fourth",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now().minusDays(1),
//                    dateEnd = LocalDateTime.now()
//                ),
//                TaskEntity(
//                    name = "fifth",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now().minusDays(1),
//                    dateEnd = LocalDateTime.now().plusDays(2)
//                ),
//                TaskEntity(
//                    name = "sixth",
//                    description = "lololol",
//                    dateStart = LocalDateTime.now().plusDays(1),
//                    dateEnd = LocalDateTime.now().plusDays(3)
//                ),
//
//                )
//            tasks.forEach { task ->
//                db.taskDao().insertTask(task)
//            }
//        }
//    }
}