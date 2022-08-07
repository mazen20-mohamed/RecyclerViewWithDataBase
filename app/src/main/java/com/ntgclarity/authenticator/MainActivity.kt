package com.ntgclarity.authenticator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.ntgclarity.authenticator.database.User
import com.ntgclarity.authenticator.database.UsersDatabase
import com.ntgclarity.authenticator.words.WordsActivity
class MainActivity : AppCompatActivity() {
    val kEmail = "signature"
    var etEmail: EditText? = null
    var etPassword: EditText? = null
    var database: UsersDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById<EditText>(R.id.et_email)
        etPassword = findViewById<EditText>(R.id.et_password)
        database = Room.databaseBuilder(this, UsersDatabase::class.java, "users.db")
            .allowMainThreadQueries()
            .build()
        loadUserEmail()
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegistration = findViewById<Button>(R.id.btn_register)
        val btnSettings = findViewById<Button>(R.id.btn_settings)
        btnRegistration.setOnClickListener {
            startRegistration()
        }
        btnLogin.setOnClickListener {
            val email = etEmail?.text.toString()
//            shared.edit()
//                .putString(kEmail, email)
//                .apply()
            val user = check()
            if (user)
            {
                startWords()
            }
            else
            {
                Toast.makeText(this, "Invalid", Toast.LENGTH_LONG).show()
            }
            //updateSignature(email)
        }
        btnSettings.setOnClickListener {
            startSettings()
        }
    }
    fun check():Boolean {
        val email = etEmail?.text.toString()
        val password = etPassword?.text.toString()
        val users: List<User>? = database?.userDao()?.getUser(email, password)
        if (users != null) {
            Toast.makeText(this, "Hello " + users[0].name, Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }
    private fun loadUserEmail() {
        //val shared = getSharedPreferences("user.prf", MODE_PRIVATE)
        val shared = PreferenceManager.getDefaultSharedPreferences(this)
        val email = shared.getString(kEmail, null)

        etEmail?.setText(email)
    }

    private fun updateSignature(text: String) {
        val defaultPref = PreferenceManager.getDefaultSharedPreferences(this)

        defaultPref.edit()
            .putString(kEmail, text)
            .apply()
    }
    fun tryFiles() {
        val filename = "hello.txt"
        val output = openFileOutput(filename, MODE_PRIVATE)

        output.write("Hello files!".toByteArray())

        val input = openFileInput(filename)
        val lines = input.bufferedReader().lineSequence()

        Log.d("###", lines.joinToString())

        val files = fileList()

        Log.d("###", files.joinToString())
    }
    private fun startRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
    private fun startWords() {
        val intent = Intent(this, WordsActivity::class.java)
        startActivity(intent)
    }
    private fun startSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}