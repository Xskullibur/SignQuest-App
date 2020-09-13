
package sg.edu.nyp.signquest.game.object;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Module {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Module() {
    }

    /**
     *
     * @param description
     * @param id
     * @param title
     * @param steps
     */
    public Module(String id, String title, String description, List<Step> steps) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

}