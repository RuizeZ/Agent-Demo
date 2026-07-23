package com.recruitmentAgent.demo.tool;

public interface Tool<T> {
    String name();

    T execute(String input);
}
