package io.skysail.server.db;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OutEdges<T extends Identifiable> implements List<T> {

    public OutEdges(String id) {
        this.id = id;
    }

    private String id;

    private String out;

    private List<T> in = new ArrayList<>();

    @Override
    public int size() {
        return in.size();
    }

    @Override
    public boolean isEmpty() {
        return in.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return in.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return in.iterator();
    }

    @Override
    public Object[] toArray() {
        return in.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return in.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return in.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return in.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return in.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return in.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return in.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return in.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return in.retainAll(c);
    }

    @Override
    public void clear() {
        in.clear();
    }

    @Override
    public T get(int index) {
        return in.get(index);
    }

    @Override
    public T set(int index, T element) {
        return in.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        in.add(index, element);
    }

    @Override
    public T remove(int index) {
        return in.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return in.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return in.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return in.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return in.subList(fromIndex, toIndex);
    }

//    public OutEdges(T entity) {
//        in = entity;
//    }



}
