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
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            transactions = mutableListOf()
            transactionAdapter = TransactionAdapter(transactions)

            val recyclerView = view.findViewById<RecyclerView>(R.id.rvTransactions)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = transactionAdapter

            loadTransactions(view, userId)
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTransactions(view: View, userId: String) {
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

                val balanceTextView = view.findViewById<TextView>(R.id.tvBalance)
                balanceTextView.text = "Balance Total: $${balanceTotal}"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar las transacciones", Toast.LENGTH_SHORT).show()
            }
    }
}



