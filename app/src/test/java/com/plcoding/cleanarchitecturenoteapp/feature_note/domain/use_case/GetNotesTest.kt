package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.plcoding.cleanarchitecturenoteapp.feature_note.data.FakeNoteRepository
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class GetNotesTest() {
    private lateinit var getNotes: GetNotes
    private lateinit var fakeNoteRepoSitory: FakeNoteRepository

    @Before
    fun setup() {
        fakeNoteRepoSitory = FakeNoteRepository()
        getNotes = GetNotes(fakeNoteRepoSitory)
        val notesToInsert = mutableListOf<Note>()

        ('a'..'z').forEach { char ->
            notesToInsert.add(
                Note(
                    title = char.toString(),
                    content = char.toString(),
                    timestamp = System.currentTimeMillis(),
                    color = 0
                )
            )
        }

        notesToInsert.shuffle()

        runBlocking {
            notesToInsert.forEach {
                fakeNoteRepoSitory.insertNote(it)
            }
        }
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
       val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size -2) {
            assertThat(notes[i].title < notes[i + 1].title)
        }
    }
}