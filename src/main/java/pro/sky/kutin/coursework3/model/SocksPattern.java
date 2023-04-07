package pro.sky.kutin.coursework3.model;

public class SocksPattern {
    private final String color;
    private final String size;
    private final Integer cottonMin;
    private final Integer cottonMax;

    private SocksPattern(String color, String size, Integer cottonMin, Integer cottonMax) {
        this.color = color;
        this.size = size;
        this.cottonMin = cottonMin;
        this.cottonMax = cottonMax;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public Integer getCottonMin() {
        return cottonMin;
    }

    public Integer getCottonMax() {
        return cottonMax;
    }

    public static SocksPattern valueOf(String color, String size, Integer cottonMin, Integer cottonMax) {
        return new SocksPattern(color, size, cottonMin, cottonMax);
    }
}
