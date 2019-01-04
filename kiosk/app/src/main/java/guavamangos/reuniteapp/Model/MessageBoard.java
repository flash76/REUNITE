package guavamangos.reuniteapp.Model;

public class MessageBoard {
    public String title;
    public String description;
    public String contentCategorization;

    public MessageBoard() {
    }

    public MessageBoard(String title, String description, String contentCategorization) {
        this.title = title;
        this.description = description;
        this.contentCategorization = contentCategorization;
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

    public String getContentCategorization() {
        return contentCategorization;
    }

    public void setContentCategorization(String contentCategorization) {
        this.contentCategorization = contentCategorization;
    }
}
