package com.yjotdev.clasificarpeces

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.yjotdev.clasificarpeces.presentation.mvvm.ui.adapter.ItemsAdapter

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    // 1. Regla de Hilt (Orden 0: se ejecuta primero para inyectar)
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // 2. Regla de Activity (Orden 1: lanza la activity después de configurar Hilt)
    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun navigationTestStartToInfoViews() {
        // --- 1. PANTALLA START ---
        // Verificamos estar en START
        onView(withId(R.id.ivCover)).check(matches(isDisplayed()))

        // Damos clic en continuar
        onView(withId(R.id.btnNext)).perform(click())

        // --- 2. PANTALLA SPECIES ---
        // Verificamos estar en SPECIES
        onView(withId(R.id.etSearch)).check(matches(isDisplayed()))

        // Damos clic en un item de la lista (RecyclerView)
        onView(withId(R.id.rvFishList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ItemsAdapter.ViewHolder>(0, click()))

        // --- 3. PANTALLA INFO ---
        // Verificamos estar en INFO
        onView(withId(R.id.ivImageUri)).check(matches(isDisplayed()))
    }
}