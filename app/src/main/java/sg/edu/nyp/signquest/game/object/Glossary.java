
package sg.edu.nyp.signquest.game.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Glossary {

    @SerializedName("value")
    @Expose
    private String value;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Glossary() {
    }

    /**
     * 
     * @param value
     */
    public Glossary(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
