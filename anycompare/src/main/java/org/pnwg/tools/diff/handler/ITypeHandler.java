package org.pnwg.tools.diff.handler;

public interface ITypeHandler<T> {

	boolean isEqual(T expected, T actual);
}
