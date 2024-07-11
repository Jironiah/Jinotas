package com.example.jinotas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.jinotas.databinding.ActivityWriteNotesBinding
import com.example.jinotas.db.AppDatabase
import com.example.jinotas.db.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class WriteNotesActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityWriteNotesBinding
    private lateinit var adapterNotes: AdapterNotes
    private lateinit var notesList: ArrayList<Note>
    private lateinit var db: AppDatabase
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWriteNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btReturnToNotes.setOnClickListener {
            val intent = Intent(this@WriteNotesActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btSaveNote.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val current = LocalDateTime.now().format(formatter)
            runBlocking {
                val corrutina = launch {
                    val note = Note(
                        null,
                        binding.etTitle.text.toString(),
                        binding.etNoteContent.text.toString(),
                        current.toString()
                    )
                    db = AppDatabase.getDatabase(this@WriteNotesActivity)
                    db.noteDAO().insertNote(note)
                  Toast.makeText(this@WriteNotesActivity, "Has creado una nota", Toast.LENGTH_LONG).show()
                }
                corrutina.join()
            }
            //Esto se puede reducir mediante una función
            val intent = Intent(this@WriteNotesActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}