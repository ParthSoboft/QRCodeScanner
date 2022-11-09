package com.qrscanner.dlapp

interface ScanningResultListener {
    fun onScanned(result: String)
}