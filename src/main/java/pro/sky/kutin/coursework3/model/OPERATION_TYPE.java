package pro.sky.kutin.coursework3.model;

public enum OPERATION_TYPE {
    ACCEPT(""), RELEASE(""), WRITEOFF("");

    private final String title;

    OPERATION_TYPE(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
