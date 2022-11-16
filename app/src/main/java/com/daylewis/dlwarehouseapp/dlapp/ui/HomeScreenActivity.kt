package com.daylewis.dlwarehouseapp.dlapp.ui

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.daylewis.dlwarehouseapp.dlapp.adapter.LocationListAdapter
import com.qrscanner.dlapp.R
import com.qrscanner.dlapp.databinding.ActivityHomeScreenBinding
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeScreenBinding
    private var selectedDate = ""
    private val cameraPermissionRequestCode = 1
    private var selectedScanningSDK = BarcodeScanningActivity.ScannerSDK.MLKIT
    private lateinit var horizontalCalendar: HorizontalCalendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeHorizontalCalendar()
        setTodayDate()

        binding?.todayDate?.setOnClickListener {
            chooseDate()
        }
        binding.todayButton.setOnClickListener {
            setTodayDate()
            horizontalCalendar.goToday(true)
        }
        binding.recyLocation.layoutManager=LinearLayoutManager(this)

    }
    fun  initData(){
        var data=ArrayList<String>()
        data.add("Rajkot,Gujarat")
        data.add("Mumbai,Gujarat")
        data.add("Surat,Gujarat")
        data.add("Buj,Gujarat")
        data.add("Jamnagar,Gujarat")
        data.add("Morbi,Gujarat")
        data.add("Bhavnagar,Gujarat")

        binding.recyLocation.adapter=LocationListAdapter(this,data){i:Int ->onClicked(i)}

    }

    private fun onClicked(i: Int) {
        selectedScanningSDK = BarcodeScanningActivity.ScannerSDK.MLKIT
        startScanning()
    }

    private fun setTodayDate() {
        val currentTime = Calendar.getInstance().time
        val today = DateFormat.format("EEEE\ndd MMM yyyy", currentTime)
        selectedDate = DateFormat.format("yyyy-MM-dd", currentTime).toString()
        binding?.todayDate?.text = today
        initData()
    }

    private fun initializeHorizontalCalendar() {

        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -12)
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 12)

        horizontalCalendar =
            HorizontalCalendar.Builder(this,R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .selectedDateBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.selected_date_background
                    )
                )
                .end()
                .build()

        horizontalCalendar.goToday(true)

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                val today = DateFormat.format("EEEE\ndd MMM yyyy", date)
                binding?.todayDate?.text = today
                selectedDate = DateFormat.format("yyyy-MM-dd", date).toString()
        //        getTodayNewsPaper(DateFormat.format("yyyy-MM-dd", date).toString())
            }
        }
    }
    private fun chooseDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
         this,R.style.DialogTheme,
            { view, year, monthOfYear, dayOfMonth ->
                var selectedMonthh = (monthOfYear + 1).toString()
                if (selectedMonthh.toInt() < 10) {
                    selectedMonthh = "0$selectedMonthh"
                }

                val tempSelectedDate = "$year-$selectedMonthh-$dayOfMonth"
                selectedDate = tempSelectedDate

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val tempDate = dateFormat.parse(tempSelectedDate)

                horizontalCalendar.selectDate(toCalendar(tempDate), true)
                val today = DateFormat.format("EEEE\ndd MMM yyyy", tempDate)
               initData()
                binding?.todayDate?.run {
                    text = today
                }
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun toCalendar(date: Date?): Calendar? {
        val cal = Calendar.getInstance()
        cal.time = date!!
        return cal
    }

    private fun startScanning() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraWithScanner()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, cameraPermissionRequestCode)
            }
        }
    }

    private fun openCameraWithScanner() {
        BarcodeScanningActivity.start(this, selectedScanningSDK)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraPermissionRequestCode) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCameraWithScanner()
            }
        }
    }
}