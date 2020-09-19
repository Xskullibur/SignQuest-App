
package sg.edu.nyp.signquest.game.object;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("glossary")
    @Expose
    private List<Glossary> glossary = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public List<Glossary> getGlossary() {
        return glossary;
    }

    public void setGlossary(List<Glossary> glossary) {
        this.glossary = glossary;
    }

}
