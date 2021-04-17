package example.android.ycb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.Factory;
import example.android.ycb.ui.main.MainViewModel;
import lombok.CustomLog;

@CustomLog
public class MainActivity extends AppCompatActivity {

  private MainViewModel mainViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    obtainViewModel();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mainViewModel.start();
  }

  private void obtainViewModel() {
    this.mainViewModel = new ViewModelProvider(this, (Factory) getApplication())
        .get(MainViewModel.class);
    log.debug("obtainViewModel, mainViewModel=%s", mainViewModel);
  }

}