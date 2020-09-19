
package sg.edu.nyp.signquest.game.gameobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Glossary implements Serializable {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("completed")
    @Expose
    private Boolean completed;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

}
