package com.project.suhail.blendr.ui.activities.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.project.suhail.blendr.R
import com.project.suhail.blendr.databinding.ItemReceiveBinding
import com.project.suhail.blendr.databinding.ItemSentBinding
import com.project.suhail.blendr.utils.FirebaseUtils
import com.project.suhail.blendr.utils.GeneralFunctions
import com.github.pgreze.reactions.ReactionPopup
import com.github.pgreze.reactions.ReactionsConfigBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(private val onClick: (view: View, imageUrl: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList = arrayListOf<Message>()
    private val reactionList = intArrayOf(
        R.drawable.ic_add_reaction,
        R.drawable.ic_fb_like,
        R.drawable.ic_fb_love,
        R.drawable.ic_fb_laugh,
        R.drawable.ic_fb_wow,
        R.drawable.ic_fb_sad,
        R.drawable.ic_fb_angry
    )
    private var senderRoom = ""
    private var receiverRoom = ""

    private inner class SentViewHolder(val mBinding: ItemSentBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    private inner class ReceiveViewHolder(val mBinding: ItemReceiveBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        if (message.senderId.equals(FirebaseAuth.getInstance().uid, true))
            return R.layout.item_sent
        return R.layout.item_receive
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.item_sent)
            return SentViewHolder(ItemSentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return ReceiveViewHolder(ItemReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messageList[position]

        val config = ReactionsConfigBuilder(holder.itemView.context).withReactions(reactionList).build()

        val popup = ReactionPopup(holder.itemView.context, config) { reactionPosition ->

            if (reactionPosition != -1) {
                if (holder is SentViewHolder)
                    holder.mBinding.ivFeeling.setImageResource(reactionList[reactionPosition])

                if (holder is ReceiveViewHolder)
                    holder.mBinding.ivFeeling.setImageResource(reactionList[reactionPosition])

                message.feeling = reactionPosition
            }


            FirebaseDatabase.getInstance().reference
                .child(FirebaseUtils.CHATS)
                .child(senderRoom)
                .child(FirebaseUtils.MESSAGES)
                .child(message.messageId)
                .setValue(message)

            FirebaseDatabase.getInstance().reference
                .child(FirebaseUtils.CHATS)
                .child(receiverRoom)
                .child(FirebaseUtils.MESSAGES)
                .child(message.messageId)
                .setValue(message)

            true
        }

        if (holder is SentViewHolder)
            holder.mBinding.apply {

                ivFeeling.setImageResource(reactionList[message.feeling])

                if (message.message.equals(FirebaseUtils.IMAGE, true)) {
                    tvSentMessage.isVisible = false
                    ivSent.isVisible = true
                    GeneralFunctions.loadImage(this.root.context, message.imageUrl, ivSent)
                } else {
                    tvSentMessage.isVisible = true
                    ivSent.isVisible = false
                    tvSentMessage.text = message.message
                }

                ivFeeling.setOnTouchListener { p0, p1 ->
                    popup.onTouch(p0!!, p1!!)
                    false
                }

                if (!message.message.equals("You deleted this message.", true)) {

                    if (!message.message.equals("This message was deleted.", true)) {

                        root.setOnLongClickListener {

                            AlertDialog.Builder(it.context)
                                .setTitle("Delete message?")
                                .setPositiveButton("Delete For Me") { dialogInterface, i ->
                                    FirebaseDatabase.getInstance().reference
                                        .child(FirebaseUtils.CHATS)
                                        .child(senderRoom)
                                        .child(FirebaseUtils.MESSAGES)
                                        .child(message.messageId)
                                        .removeValue()
                                }
                                .setNegativeButton("Cancel") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                }
                                .setNeutralButton("Delete For Everyone") { dialogInterface, i ->

                                    message.message = "You deleted this message."

                                    FirebaseDatabase.getInstance().reference
                                        .child(FirebaseUtils.CHATS)
                                        .child(senderRoom)
                                        .child(FirebaseUtils.MESSAGES)
                                        .child(message.messageId)
                                        .setValue(message)

                                    message.message = "This message was deleted."

                                    FirebaseDatabase.getInstance().reference
                                        .child(FirebaseUtils.CHATS)
                                        .child(receiverRoom)
                                        .child(FirebaseUtils.MESSAGES)
                                        .child(message.messageId)
                                        .setValue(message)
                                }
                                .show()
                            true
                        }
                    }
                }

                root.setOnClickListener {
                    if (message.message.equals(FirebaseUtils.IMAGE, true)) {
                        onClick(root, message.imageUrl)
                    }
                }
            }

        if (holder is ReceiveViewHolder)
            holder.mBinding.apply {

                ivFeeling.setImageResource(reactionList[message.feeling])

                if (message.message.equals(FirebaseUtils.IMAGE, true)) {
                    tvReceivedMessage.isVisible = false
                    ivReceive.isVisible = true
                    GeneralFunctions.loadImage(this.root.context, message.imageUrl, ivReceive)
                } else {
                    tvReceivedMessage.isVisible = true
                    ivReceive.isVisible = false
                    tvReceivedMessage.text = message.message
                }

                ivFeeling.setOnTouchListener { p0, p1 ->
                    popup.onTouch(p0!!, p1!!)
                    false
                }

                if (!message.message.equals("This message was deleted.", true)) {

                    root.setOnLongClickListener {

                        AlertDialog.Builder(it.context)
                            .setTitle("Delete message?")
                            .setPositiveButton("Delete For Me") { dialogInterface, i ->
                                FirebaseDatabase.getInstance().reference
                                    .child(FirebaseUtils.CHATS)
                                    .child(senderRoom)
                                    .child(FirebaseUtils.MESSAGES)
                                    .child(message.messageId)
                                    .removeValue()
                            }
                            .setNegativeButton("Cancel") { dialogInterface, i ->
                                dialogInterface.dismiss()
                            }
                            .show()
                        true
                    }
                }

                root.setOnClickListener {
                    if (message.message.equals(FirebaseUtils.IMAGE, true)) {
                        onClick(root, message.imageUrl)
                    }
                }
            }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setData(messageList: ArrayList<Message>, senderRoom: String, receiverRoom: String) {
        this.messageList = messageList
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
        notifyDataSetChanged()
    }
}