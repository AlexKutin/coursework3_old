package pro.sky.kutin.coursework3.model;

public enum SOCKS_COLOR {
    BLACK("Черные"), WHITE("Белые"), BROWN("Коричневые"), YELLOW("Голубые"), BLUE("Синие");

    private final String title;

    SOCKS_COLOR(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
