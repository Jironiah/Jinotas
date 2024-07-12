package com.example.jinotas

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.jinotas.databinding.ActivityShowNoteBinding
import com.example.jinotas.db.AppDatabase
import com.example.jinotas.db.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class ShowNoteActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityShowNoteBinding
    private lateinit var notesShow: Note
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
        binding = ActivityShowNoteBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        var idSearchUpdate = intent.getIntExtra("id", 0)
        runBlocking {
            val corrutina = launch {
                db = AppDatabase.getDatabase(this@ShowNoteActivity)
                notesShow = db.noteDAO().getNoteById(idSearchUpdate)
            }
            corrutina.join()
        }

        //Esto coloca el titulo y el contenido de la nota
        binding.etTitle.setText(notesShow.title)
        binding.etNoteContent.setText(notesShow.textContent)

        binding.btReturnToNotes.setOnClickListener {
            finish()
        }

        binding.btOverwriteNote.setOnClickListener {
            runBlocking {
                val corrutina = launch {
                    db = AppDatabase.getDatabase(this@ShowNoteActivity)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val current = LocalDateTime.now().format(formatter).toString()
                    val noteUpdate = Note(
                        idSearchUpdate,
                        binding.etTitle.text.toString(),
                        binding.etNoteContent.text.toString(),
                        current
                    )
                    db.noteDAO().updateNote(noteUpdate)
                    Toast.makeText(
                        this@ShowNoteActivity, "Has modificado la nota", Toast.LENGTH_SHORT
                    ).show()
                }
                corrutina.join()
            }
            finish()
        }
        setContentView(binding.root)
    }
}