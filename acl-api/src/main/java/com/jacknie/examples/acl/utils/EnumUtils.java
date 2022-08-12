package com.jacknie.examples.acl.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public abstract class EnumUtils {

  private EnumUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 해당 Enum 클래스 객체로 변환
   * @param value 매개 변수 문자열
   * @param enumClass 변경 Enum 클래스
   * @param <E> Enum 타입 매개 변수
   * @return 해당 Enum 객체
   */
  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> Optional<E> parseEnum(@Nullable String value, Class<E> enumClass) {
    MethodInvoker invoker = new MethodInvoker();
    invoker.setTargetClass(enumClass);
    invoker.setStaticMethod(enumClass.getName() + ".valueOf");
    invoker.setArguments(value);
    try {
      invoker.prepare();
      Object result = invoker.invoke();
      return Optional.ofNullable((E) result);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof IllegalArgumentException) {
        return Optional.empty();
      } else {
        throw new IllegalStateException(e);
      }
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

}
