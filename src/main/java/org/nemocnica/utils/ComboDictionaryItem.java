package org.nemocnica.utils;

import java.util.Objects;

//equals tylko po id - bardzo przydatne czy ustawianiu selekcji w combo jak mamy tylko id na wejsciu
public class ComboDictionaryItem {
    private String name;
    private Integer id;

    public ComboDictionaryItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComboDictionaryItem that = (ComboDictionaryItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
