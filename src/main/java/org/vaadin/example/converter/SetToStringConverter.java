package org.vaadin.example.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.HashSet;
import java.util.Set;

public class SetToStringConverter implements Converter<Set<String>, String> {
    @Override
    public Result<String> convertToModel(Set<String> set, ValueContext valueContext) {
        return Result.ok(String.join(",", set));
    }

    @Override
    public Set<String> convertToPresentation(String s, ValueContext valueContext) {
        if (s == null) return new HashSet<>();
        return Set.of(s.split(","));
    }
}
