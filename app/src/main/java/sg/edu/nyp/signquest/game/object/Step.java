
package sg.edu.nyp.signquest.game.object;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("glossary")
    @Expose
    private List<Glossary> glossary = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step() {
    }

    /**
     * 
     * @param glossary
     * @param completed
     */
    public Step(Boolean completed, List<Glossary> glossary) {
        super();
        this.completed = completed;
        this.glossary = glossary;
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
