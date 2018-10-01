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

    private val baseUrl = "https://2f626a2c.ngrok.io"

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

        val statusTask = LoadStatusTask(this)
        statusTask.execute()
    }

    class LoadTemperatureTask(private val activity: MainActivity) : AsyncTask<String, Void, String>() {

        private val url : String = activity.baseUrl + "/temperature"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            Log.e("response", text)
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

        private val url : String = activity.baseUrl + "/humidity"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            Log.e("response", text)
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

        private val url : String = activity.baseUrl + "/air_pollution"

        override fun doInBackground(vararg uri: String): String? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            Log.e("response", text)
            val fromJson = Gson().fromJson(text, AirPollution::class.java)
            return fromJson.air_pollution

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val str = "$result ppm"
            activity.monitor_air_pollution.text = str
        }

    }

    class LoadStatusTask(private val activity: MainActivity) : AsyncTask<String, Void, Int>() {

        private val url : String = activity.baseUrl + "/status"

        override fun doInBackground(vararg uri: String): Int? {

            val connection = URL(url).openConnection() as HttpURLConnection
            Log.e("responseCode", connection.responseCode.toString())
            val text = connection.inputStream.bufferedReader().readText()
            Log.e("response", text)
            val fromJson = Gson().fromJson(text, com.github.valkoz.chargemonitor.Status::class.java)
            return fromJson.status

        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (result!! == 1) {
                activity.status.text = "Is used"
                activity.status.setTextColor(activity.resources.getColor(R.color.yellow))
            }
            else {
                activity.status.text = "Availible"
                activity.status.setTextColor(activity.resources.getColor(R.color.green))
            }
        }

    }

}
