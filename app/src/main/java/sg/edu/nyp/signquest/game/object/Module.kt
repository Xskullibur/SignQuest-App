package sg.edu.nyp.signquest.game.`object`

import android.os.Parcel
import android.os.Parcelable

class Module(var id: String, var title: String, var description: String): Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
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