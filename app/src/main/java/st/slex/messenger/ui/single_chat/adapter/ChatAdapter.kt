package st.slex.messenger.ui.single_chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.messenger.data.model.MessageModel

class ChatAdapter(private val uid: String) : RecyclerView.Adapter<ChatViewHolder>() {

    private var listMessages = mutableListOf<MessageModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerSingleChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder((binding))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = listMessages[position]
        if (message.user.id == uid) {
            holder.bindUser(message)
        } else {
            holder.bindReceiver(message)
        }

    }

    override fun getItemCount(): Int = listMessages.size

    fun addItemToBottom(item: MessageModel, onSuccess: () -> Unit) {
        if (!listMessages.contains(item)) {
            listMessages.add(item)
            notifyItemInserted(listMessages.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: MessageModel, onSuccess: () -> Unit) {
        if (!listMessages.contains(item)) {
            listMessages.add(item)
            listMessages.sortBy { it.timestamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

}