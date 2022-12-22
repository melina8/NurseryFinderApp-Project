package com.example.nurseryfinder.Model

import android.os.Parcel
import android.os.Parcelable


data class Nursery(
    var uid: String, var name: String, var phone: String, var email: String, var activities: String, var image: String, var prefecture: String,
    var municipality: String, var address: String, var ages: String, var espa: String, var food: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    constructor(): this("", "", "", ", ", ", ", ", ", ", ", ", ", ", ", "", "", "")


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(activities)
        parcel.writeString(image)
        parcel.writeString(prefecture)
        parcel.writeString(municipality)
        parcel.writeString(address)
        parcel.writeString(ages)
        parcel.writeString(espa)
        parcel.writeString(food)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Nursery> {
        override fun createFromParcel(parcel: Parcel): Nursery {
            return Nursery(parcel)
        }

        override fun newArray(size: Int): Array<Nursery?> {
            return arrayOfNulls(size)
        }
    }


}