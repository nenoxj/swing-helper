package cn.note.swing.core.view.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 当仅有一个值时 key=value
 * key 与value 作为唯一性判断
 *
 * @author jee
 * @version 1.0
 */
@Getter
@Setter
public class SelectedItem {

    private String key;
    private String value;


    public SelectedItem(String key) {
        this(key, key);
    }


    public SelectedItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedItem that = (SelectedItem) o;
        return key.equals(that.key) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
