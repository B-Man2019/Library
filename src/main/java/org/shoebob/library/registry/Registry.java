package org.shoebob.library.registry;

import org.shoebob.library.interfaces.RegistryItem;

import java.util.ArrayList;

public class Registry<E extends RegistryItem> {
    private ArrayList<E> registryList = new ArrayList<E>();

    public ArrayList<E> get() {
        return registryList;
    }

    public E get(int index) {
        return registryList.get(index);
    }

    public void remove(int index) {
        registryList.remove(index);
    }

    public E add(E object) {
        registryList.add(object);
        return object;
    }
}
