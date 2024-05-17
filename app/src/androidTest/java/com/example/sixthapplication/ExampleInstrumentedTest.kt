package com.example.sixthapplication

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.opencsv.CSVWriter
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.sixthapplication", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class UIAutomatorTest {
    @Test
    fun selectFirstVideoFromGallery() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val numImagesInAlbum =6
        for(i in 5 until numImagesInAlbum){
            val context = ApplicationProvider.getApplicationContext<Context>()
            val intent = context.packageManager.getLaunchIntentForPackage("com.example.sixthapplication")

            context.startActivity(intent)


            device.wait(Until.hasObject(By.res("com.example.sixthapplication", "selectButton")), 5000)
            val selectVideoButton = device.findObject(UiSelector().resourceId("com.example.sixthapplication:id/selectButton"))
            selectVideoButton.click()

            device.wait(Until.hasObject(By.pkg("com.android.gallery3d")), 5000)
            val albumName = "Whatsapp"
            val albumSelector = UiSelector().text(albumName)
            val album = device.findObject(albumSelector)
            album.click()

            device.wait(Until.hasObject(By.pkg("com.android.gallery3d")), 5000)

            val imageSelector = UiSelector().className("android.widget.ImageView").index(i)
            val image = device.findObject(imageSelector)
            image.click()

            device.wait(Until.hasObject(By.res("com.example.sixthapplication", "predictButton")), 5000)
            val predictButton = device.findObject(UiSelector().resourceId("com.example.sixthapplication:id/predictButton"))
            predictButton.click()

            device.wait(Until.hasObject(By.res("com.example.sixthapplication:id/predictedClassText")), 5000)
            val predictionResult = device.findObject(UiSelector().resourceId("com.example.sixthapplication:id/predictedClassText"))
                .text
            println(predictionResult)
        }

    }



}

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Test
    fun testSelectVideoButton() {
        for(i in 33 until  59){
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("selectFromAssets", true)
                putExtra("imageFileName", i.toString()+".JPEG")
            }
            ActivityScenario.launch<MainActivity>(intent)

            onView(withId(R.id.selectButton)).perform(click())
//            Thread.sleep(1000)
            onView(withId(R.id.predictButton)).perform(click())
//            Thread.sleep(1000)
        }


    }
}
