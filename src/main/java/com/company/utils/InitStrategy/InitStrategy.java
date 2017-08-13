package com.company.utils.InitStrategy;

/**
 * Created by Home on 13.08.2017.
 */
public interface InitStrategy<T> {
    public String getFileName();

    public T parseLine(String[] parts);
}
