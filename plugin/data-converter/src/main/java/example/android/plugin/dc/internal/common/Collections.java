package example.android.plugin.dc.internal.common;


import com.google.common.base.Preconditions;
import java.util.Collection;

public abstract class Collections {

  public static <C> void consumeRemaining(Collection<C> collection, Consumer<C> consumer) {
    Preconditions.checkNotNull(collection);
    Preconditions.checkNotNull(consumer);
    for (C c : collection) {
      consumer.accept(c);
    }
  }

  public static <C> void consumeRemaining(C[] dataArr, Consumer<C> consumer) {
    Preconditions.checkNotNull(dataArr);
    Preconditions.checkNotNull(consumer);
    for (C c : dataArr) {
      consumer.accept(c);
    }
  }

  public static <C> void consumeRemaining(Iterable<C> collection, Consumer<C> consumer) {
    Preconditions.checkNotNull(collection);
    Preconditions.checkNotNull(consumer);
    for (C c : collection) {
      consumer.accept(c);
    }
  }

  public static boolean isNullOrEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }
}
