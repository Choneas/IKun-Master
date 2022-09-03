package cn.feng.ikun.value;

public class ListValue extends Value {
    private final String[] values;
    private String value;
    public ListValue(String name, String[] values, String value) {
        super(name);
        this.values = values;
        this.value = value;
    }

    public String[] getValues() {
        return values;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getModeListNumber(String name) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(name)) {
                return  i;
            }
        }
        return 0;
    }

    public boolean isCurrentMode(String mode) {
        return value.equalsIgnoreCase(mode);
    }
}
