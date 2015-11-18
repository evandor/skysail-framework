package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InEdge<T extends Identifiable> implements List<T> {

    public InEdge(String id) {
        this.id = id;
    }

    private String id;

    private String in;

    private List<T> out = new ArrayList<>();

    @Override
    public int size() {
        return out.size();
    }

    @Override
    public boolean isEmpty() {
        return out.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return out.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return out.iterator();
    }

    @Override
    public Object[] toArray() {
        return out.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return out.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return out.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return out.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return out.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return out.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return out.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return out.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return out.retainAll(c);
    }

    @Override
    public void clear() {
        out.clear();
    }

    @Override
    public T get(int index) {
        return out.get(index);
    }

    @Override
    public T set(int index, T element) {
        return out.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        out.add(index, element);
    }

    @Override
    public T remove(int index) {
        return out.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return out.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return out.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return out.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return out.subList(fromIndex, toIndex);
    }

}
