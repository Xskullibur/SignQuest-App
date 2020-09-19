package sg.edu.nyp.signquest.game.gameobject

import android.os.Parcel
import android.os.Parcelable

data class Gloss(var value: String) : Parcelable {

    var completed: Boolean = false

    constructor(parcel: Parcel): this(parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gloss> {
        override fun createFromParcel(parcel: Parcel): Gloss {
            return Gloss(parcel)
        }

        override fun newArray(size: Int): Array<Gloss?> {
            return arrayOfNulls(size)
        }
    }
}