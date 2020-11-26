package cn.edu.seu.xzp.movie.object;

public class Cond {
    private String key;
    private String option;
    private Object value;
    public Cond(String key, String option, Object value) {
        this.key = key;
        this.option = option;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public String getOption() {
        return option;
    }

    public Object getValue() {
        return value;
    }
}
