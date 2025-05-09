package com.cogu.spylook.model.utils.converters

import android.app.Activity
import android.content.Intent
import com.cogu.spylook.model.SingleExportObject

object SingleExportObjectSaver {

    fun save(singleExportObject: SingleExportObject) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "export.json")
        }

    }
}