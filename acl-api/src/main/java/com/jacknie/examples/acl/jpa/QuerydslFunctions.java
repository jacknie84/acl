package com.jacknie.examples.acl.jpa;

import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

public abstract class QuerydslFunctions {

    private QuerydslFunctions() {
        throw new UnsupportedOperationException();
    }

    public static NumberTemplate<Integer> bitand(NumberPath<Integer> path, int value) {
        return numberTemplate(Integer.class, "bitand({0}, {1})", path, value);
    }
}
