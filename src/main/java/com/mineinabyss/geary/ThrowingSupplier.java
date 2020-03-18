package com.mineinabyss.geary;

/**
 * General purpose supplier that wraps checked exceptions.
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {

  T get() throws Exception;

  static <T> T unchecked(ThrowingSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}