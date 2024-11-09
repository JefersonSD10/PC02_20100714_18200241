package dev.jj.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddTransactionFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        val dateEditText = view.findViewById<EditText>(R.id.etDate)
        val descriptionEditText = view.findViewById<EditText>(R.id.etDescription)
        val amountEditText = view.findViewById<EditText>(R.id.etAmount)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        saveButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0

            saveTransaction(date, description, amount)
        }
    }

    private fun saveTransaction(date: String, description: String, amount: Double) {
        val transaction = hashMapOf(
            "date" to date,
            "description" to description,
            "amount" to amount
        )

        db.collection("users").document(userId).collection("movements")
            .add(transaction)
            .addOnSuccessListener {
                Toast.makeText(context, "Transacción guardada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }
}
