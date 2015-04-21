package io.skysail.server.converter.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import lombok.ToString;
import de.twenty11.skysail.server.core.FormField;

@ToString(of = { "fields" })
public class STFieldsWrapper implements List<FormField> {

    private List<FormField> fields = new ArrayList<>();

    public STFieldsWrapper(List<FormField> fields) {
        this.fields = fields;
    }

    public boolean hasMarkdownEditor() {
        return fields.stream().filter(f -> {
            return f.isMarkdownEditorInputType();
        }).findFirst().isPresent();
    }

    @Override
    public int size() {
        return fields.size();
    }

    @Override
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return fields.contains(o);
    }

    @Override
    public Iterator<FormField> iterator() {
        if (fields == null) {
            return null;
        }
        return fields.iterator();
    }

    @Override
    public Object[] toArray() {
        return fields.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return fields.toArray(a);
    }

    @Override
    public boolean add(FormField e) {
        return fields.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return fields.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return fields.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends FormField> c) {
        return fields.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends FormField> c) {
        return fields.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return fields.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return fields.retainAll(c);
    }

    @Override
    public void clear() {
        fields.clear();
    }

    @Override
    public FormField get(int index) {
        return fields.get(index);
    }

    @Override
    public FormField set(int index, FormField element) {
        return fields.set(index, element);
    }

    @Override
    public void add(int index, FormField element) {
        fields.add(index, element);
    }

    @Override
    public FormField remove(int index) {
        return fields.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return fields.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return fields.lastIndexOf(o);
    }

    @Override
    public ListIterator<FormField> listIterator() {
        return fields.listIterator();
    }

    @Override
    public ListIterator<FormField> listIterator(int index) {
        return fields.listIterator(index);
    }

    @Override
    public List<FormField> subList(int fromIndex, int toIndex) {
        return fields.subList(fromIndex, toIndex);
    }

}
