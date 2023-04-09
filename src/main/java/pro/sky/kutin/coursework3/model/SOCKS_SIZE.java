package pro.sky.kutin.coursework3.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SOCKS_SIZE {
    SIZE_36("36"), SIZE_37("37"), SIZE_375("37,5"), SIZE_38("38"), SIZE_39("39"), SIZE_40("40"), SIZE_41("41"), SIZE_42("42");

    private final String size;

    SOCKS_SIZE(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return size;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SOCKS_SIZE fromText(String text) {
        for (SOCKS_SIZE r : SOCKS_SIZE.values()) {
            if (r.getSize().equals(text) || r.name().equals(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException();
    }
}
