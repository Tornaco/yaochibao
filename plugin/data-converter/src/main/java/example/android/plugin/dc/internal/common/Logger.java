package example.android.plugin.dc.internal.common;

public abstract class Logger {

  public static void debug(String message, Object... obj) {
    System.out.println("[Data-Converter] " + String.format(message, obj));
  }

  public static void report(String message, Object... obj) {
    report(new Throwable(), message, obj);
  }

  public static void report(Throwable throwable, String message, Object... obj) {
    System.err.println(String.format(message, obj));
    printStackTrace(throwable);
  }

  public static void printStackTrace(Throwable throwable) {
    if (throwable == null) {
      return;
    }
    throwable.printStackTrace();
  }
}
