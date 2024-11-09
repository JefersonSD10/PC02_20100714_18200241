package dev.jj.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class TransactionListFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactions: MutableList<Transaction>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        transactions = mutableListOf()
        transactionAdapter = TransactionAdapter(transactions)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = transactionAdapter

        loadTransactions(view)
    }

    private fun loadTransactions(view: View) {
        db.collection("users").document(userId).collection("movements")
            .get()
            .addOnSuccessListener { result ->
                var balanceTotal = 0.0
                transactions.clear()
                for (document in result) {
                    val transaction = document.toObject(Transaction::class.java)
                    transactions.add(transaction)
                    balanceTotal += transaction.amount
                }
                transactionAdapter.notifyDataSetChanged()

                // Actualizar el balance total en el TextView
                val balanceTextView = view.findViewById<TextView>(R.id.tvBalance)
                balanceTextView.text = "Balance Total: $${balanceTotal}"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar las transacciones", Toast.LENGTH_SHORT).show()
            }
    }
}


