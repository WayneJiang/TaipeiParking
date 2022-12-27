package com.wayne.taipeiparking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wayne.taipeiparking.databinding.LayoutParkingLotItemBinding
import com.wayne.taipeiparking.repository.entity.ParkingInfoEntity

class ParkingLotAdapter() :
    ListAdapter<ParkingInfoEntity, ParkingLotAdapter.ParkingLotViewHolder>(
        ParkingInfoDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotViewHolder {
        return ParkingLotViewHolder(
            LayoutParkingLotItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParkingLotViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ParkingLotViewHolder(private val layoutParkingLotItemBinding: LayoutParkingLotItemBinding) :
        RecyclerView.ViewHolder(layoutParkingLotItemBinding.root) {
        fun bind(parkingInfoEntity: ParkingInfoEntity) = with(layoutParkingLotItemBinding) {
            parkingInfoEntity.apply {
                tvId.text = itemView.context.getString(R.string.parking_id, id)
                tvName.text = itemView.context.getString(R.string.parking_name, name)
                tvAddress.text = itemView.context.getString(R.string.parking_address, address)
                tvTotal.text = itemView.context.getString(R.string.parking_total, total)

                if ((available >= 0) or (charing >= 0) or (standby >= 0)) {
                    tvAvailable.text =
                        itemView.context.getString(R.string.parking_available, available)
                    tvStandby.text = itemView.context.getString(R.string.parking_standby, standby)
                    tvCharging.text = itemView.context.getString(R.string.parking_charging, charing)
                } else {
                    tvAvailable.visibility = View.GONE
                    tvStandby.visibility = View.GONE
                    tvCharging.visibility = View.GONE
                }
            }
        }
    }
}

private class ParkingInfoDiffUtilCallback : DiffUtil.ItemCallback<ParkingInfoEntity>() {
    override fun areItemsTheSame(oldItem: ParkingInfoEntity, newItem: ParkingInfoEntity) =
        (newItem.id == oldItem.id)

    override fun areContentsTheSame(
        oldItem: ParkingInfoEntity,
        newItem: ParkingInfoEntity
    ): Boolean =
        (newItem.available == oldItem.available) and
                (newItem.charing == oldItem.charing) and
                (newItem.standby == oldItem.standby)
}