package com.sherman.trovit.domain;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Denis M. Gabaydulin
 * @since 07.04.19
 */
public class CarAdGroup {
    private CarAdUniqueKey key;
    private Set<String> uniqueKeys = new HashSet<>();

    public CarAdUniqueKey getKey() {
        return key;
    }

    public void setKey(CarAdUniqueKey key) {
        this.key = key;
    }

    public Set<String> getUniqueKeys() {
        return uniqueKeys;
    }

    public void setUniqueKeys(Set<String> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarAdGroup carAdGroup = (CarAdGroup) o;
        return Objects.equal(key, carAdGroup.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("key", key)
            .add("uniqueKeys", uniqueKeys)
            .toString();
    }
}
