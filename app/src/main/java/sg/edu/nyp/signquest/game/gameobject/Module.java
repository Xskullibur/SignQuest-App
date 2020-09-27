
package sg.edu.nyp.signquest.game.gameobject;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class Module implements Serializable {

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

    public boolean isCompleted() {
        return steps.stream().allMatch(Step::getCompleted);
    }

    @Nullable
    public Step nextIncompleteStep() {
        Optional<Step> value = steps.stream().filter(x -> !x.getCompleted()).findFirst();
        return value.orElse(null);
    }

}
