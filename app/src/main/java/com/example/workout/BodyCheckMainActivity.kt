package com.example.workout
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.workout.DataType.RecordOfDay
import com.example.workout.helper.Helper
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.io.File

class BodyCheckMainActivity : AppCompatActivity() {
    private lateinit var _btnLoadPic : TextView
    private lateinit var _btnSavePic : Button
    private lateinit var _imgView: ImageView
    private lateinit var _weightInput: TextInputEditText

    private lateinit var _launcher : ActivityResultLauncher<Intent>

    private var _imgPath : String? = null
    private var _weight : String = ""

    private var _allDayRecord : MutableMap<String, RecordOfDay>? = null
    private var _todayRecord : RecordOfDay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_check_main)

        _btnLoadPic = findViewById(R.id.btn_load_img)
        _btnSavePic = findViewById(R.id.btn_record_body)
        _imgView = findViewById(R.id.imageView)
        _weightInput = findViewById(R.id.weightInput)
        _allDayRecord = getAllDayRecord()
        _todayRecord = getTodayRecordFromRunTime()
        _weight = _todayRecord?.weight?:""
        _imgPath = _todayRecord?.picturePath?:""

        _launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (null == activityResult.data) {
                return@registerForActivityResult
            }
            val intent = activityResult.data
            val path = getRealPathFromURI(intent?.data!!)
            _imgPath = path

            val imgFile = File(path)

            if (imgFile.exists()) {
                _imgView.setImageURI(Uri.fromFile(imgFile))
            }
        }

        _weightInput.setText(_weight)
        val imgFile = File(_imgPath)
        if (imgFile.exists()) {
            _imgView.setImageURI(Uri.fromFile(imgFile))
        }

        _btnLoadPic.setOnClickListener {
            var isPermissioned = ContextCompat.checkSelfPermission(this@BodyCheckMainActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (PackageManager.PERMISSION_GRANTED != isPermissioned) {
                ActivityCompat.requestPermissions(this@BodyCheckMainActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            } else {
                goToGallery();
            }
        }

        _btnSavePic.setOnClickListener {
            // 이미지 저장
            _todayRecord?.weight = _weightInput.text.toString()
            _todayRecord?.picturePath = _imgPath
            _allDayRecord?.let {
                pushAllDayRecordToSP(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            goToGallery();
        }
    }

    fun goToGallery() {
        // 이미지 선택창으로 이동
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        _launcher.launch(intent)
    }

    fun getAllDayRecord() : MutableMap<String, RecordOfDay>? {
        return Helper.getFromSP("AllDayRecord", "record", this)
    }

    fun pushAllDayRecordToSP(record : MutableMap<String, RecordOfDay>){
        Helper.pushToSP(record, "AllDayRecord", "record", this)
    }

    fun getTodayRecordFromRunTime() : RecordOfDay? {
        var current = LocalDate.now().toString()
        return _allDayRecord?.get(current)
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}