package com.github.valkoz.chargemonitor

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refresh.setOnClickListener {
            update()
        }
        update()
    }

    private fun update() {
        val temperatureTask = LoadTemperatureTask(this)
        temperatureTask.execute()

        val humidityTask = LoadHumidityTask(this)
        humidityTask.execute()

        val airPollutionTask = LoadAirPollutionTask(this)
        airPollutionTask.execute()
    }

    class LoadTemperatureTask(private val activity: MainActivity) : AsyncTask<String, Void, String>() {

        private val url : String = "https://6c1fe2e2.ngrok.io/temperature"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            val fromJson = Gson().fromJson(text, Temperature::class.java)
            return fromJson.temperature

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val str = "$result°C"
            activity.monitor_temperature.text = str
        }

    }

    class LoadHumidityTask(private val activity: MainActivity) : AsyncTask<String, Void, String>() {

        private val url : String = "https://6c1fe2e2.ngrok.io/humidity"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            val fromJson = Gson().fromJson(text, Humidity::class.java)
            return fromJson.humidity

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val str = "$result%"
            activity.monitor_humidity.text = str
        }

    }

    class LoadAirPollutionTask(private val activity: MainActivity) : AsyncTask<String, Void, String>() {

        private val url : String = "https://6c1fe2e2.ngrok.io/air_pollution"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            val fromJson = Gson().fromJson(text, AirPollution::class.java)
            return fromJson.airPollution

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val str = "$result ppm"
            activity.monitor_air_pollution.text = str
        }

    }

}