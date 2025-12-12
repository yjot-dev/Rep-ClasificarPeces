package com.yjotdev.clasificarpeces

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.app.Instrumentation.ActivityResult
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal // Importante
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not // Importante
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testFlujoCompletoDeNavegacion() {
        // 1. PANTALLA START
        onView(withId(R.id.btnNext)).check(matches(isDisplayed()))
        onView(withId(R.id.btnNext)).perform(click())

        // 2. PANTALLA DETECTOR
        onView(withId(R.id.btnChoosePhoto)).check(matches(isDisplayed()))

        // Preparacion de imagen Fake
        val resultData = Intent()
        resultData.data = createFakeImageUri()
        val result = ActivityResult(Activity.RESULT_OK, resultData)

        // Intercepta cualquier intent que no sea interno de la app
        intending(not(isInternal())).respondWith(result)

        // Simular Clic al elegir foto de galeria
        onView(withId(R.id.btnChoosePhoto)).perform(click())

        // Verificamos que la imagen se cargó
        waitForView(R.id.imgPhoto)

        // Simular Clic al detectar imagen
        onView(withId(R.id.btnDetector)).perform(click())

        // Verificamos que la lista se cargo
        waitForView(R.id.listDetector)

        // Simular Clic al topar la lista
        onView(withId(R.id.listDetector)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // 3. PANTALLA INFO
        onView(withId(R.id.txtName)).check(matches(isDisplayed()))
        onView(withId(R.id.txtDescription)).check(matches(isDisplayed()))
    }

    // Función auxiliar para esperar hasta que una vista sea visible
    private fun waitForView(viewId: Int, timeout: Long = 5000) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeout

        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()))
                return // Si pasa el check, salimos
            } catch (_: Exception) {
                Thread.sleep(100) // Esperamos un poco y reintentamos
            }
        }
        // Si se acaba el tiempo, lanzamos la excepción original intentando una última vez
        onView(withId(viewId)).check(matches(isDisplayed()))
    }

    // Función auxiliar para crear una imagen real temporal
    private fun createFakeImageUri(): android.net.Uri {
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val file = File(context.cacheDir, "test_image.png")

        // Creamos un bitmap y lo guardamos en el archivo
        val bitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888)
        // Píntalo de un color para que no sea transparente (opcional)
        bitmap.eraseColor(android.graphics.Color.BLUE)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return android.net.Uri.fromFile(file)
    }
}