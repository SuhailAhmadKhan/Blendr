package com.project.suhail.blendr.ui.activities.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.suhail.blendr.databinding.ItemConversationBinding
import com.project.suhail.blendr.models.User
import com.project.suhail.blendr.utils.BaseActivity
import com.project.suhail.blendr.utils.Constants
import com.project.suhail.blendr.utils.FirebaseUtils
import com.project.suhail.blendr.utils.GeneralFunctions
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class UserAdapter(val onClick: (view: View, position: Int) -> Unit) :
    RecyclerView.Adapter<UserAdapter.ConversionViewHolder>() {

    private var userList = arrayListOf<User>()

    inner class ConversionViewHolder(val mBinding: ItemConversationBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        return ConversionViewHolder(
            ItemConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        val user = userList[position]

        val senderId = FirebaseAuth.getInstance().uid

        val senderRoom = user.uid + senderId
        val receiverRoom = senderId + user.uid

        ((holder.itemView.context) as BaseActivity).getChildValues(
            FirebaseUtils.CHATS,
            senderRoom
        ) { snapshot ->
            if (snapshot.exists()) {

                val lastMsg = snapshot.child(FirebaseUtils.LAST_MSG).getValue(String::class.java)
                val lastMsgTime = snapshot.child(FirebaseUtils.LAST_MSG_TIME).getValue(Long::class.java)


                lastMsg?.let {
                    holder.mBinding.tvLastMessage.text = it
                }

                lastMsgTime?.let {
                    holder.mBinding.tvMessageTime.text = Constants.simpleTimeFormat.format(Date(it))
                }


            } else
                holder.mBinding.tvLastMessage.text = "Tap to chat"
        }

        holder.apply {
            mBinding.root.setOnClickListener { onClick(mBinding.root, position) }
            mBinding.ivProfilePic.setOnClickListener { onClick(mBinding.ivProfilePic, position) }

            mBinding.tvUsername.text = user.name
            GeneralFunctions.loadImage(
                holder.itemView.context,
                user.profileImage,
                mBinding.ivProfilePic
            )
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(userList: ArrayList<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }
}