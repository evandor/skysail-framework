package io.skysail.server.converter.wrapper;

import io.skysail.api.responses.*;

import java.util.*;

import lombok.ToString;

@ToString
public class STListSourceWrapper implements List<Object> {

    private List<Object> source;

    public STListSourceWrapper(List<Object> source) {
        this.source = source;
    }

    public String getEntityType() {
        if (this.source.isEmpty()) {
            return "none";
        }
        return this.source.get(0).getClass().getName();
    }

    public boolean isForm() {
        if (source.get(0) instanceof FormResponse) {
            return ((SkysailResponse<?>) source.get(0)).isForm();
        }
        return false;
    }

    public boolean isList() {
        return true;
    }

    public String getFormTarget() {
        if (source.get(0) instanceof FormResponse) {
            return ((FormResponse<?>) source.get(0)).getTarget();
        }
        return null;
    }

   

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return source.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return source.iterator();
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return source.toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return source.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return source.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return source.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return source.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return source.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return source.retainAll(c);
    }

    @Override
    public void clear() {
        source.clear();
    }

    @Override
    public Object get(int index) {
        return source.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return source.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        source.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return source.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return source.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return source.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return source.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return source.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return source.subList(fromIndex, toIndex);
    }

}
