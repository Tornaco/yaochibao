package example.android.ycb.infra.log;

import android.util.Log;
import lombok.AllArgsConstructor;

public interface Logger {

  void debug(String format, Object... args);
  void error(String format, Object... args);

  static Logger getLogger(Class<?> clazz) {
    return new Impl(clazz.getName());
  }

  @AllArgsConstructor
  class Impl implements Logger {

    private final String tag;

    @Override
    public void debug(String format, Object... args) {
      Log.d("YCB", "[" + tag + "]: " + String.format(format, args));
    }

    @Override
    public void error(String format, Object... args) {
      Log.e("YCB", "[" + tag + "]: " + String.format(format, args));
    }
  }
}
