// Generated code from Butter Knife. Do not modify!
package id.rumahawan.manajemenkomplain;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.ivExit = Utils.findRequiredViewAsType(source, R.id.ivExit, "field 'ivExit'", ImageView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tvName, "field 'tvName'", TextView.class);
    target.tvPosition = Utils.findRequiredViewAsType(source, R.id.tvPosition, "field 'tvPosition'", TextView.class);
    target.rrComplaint = Utils.findRequiredViewAsType(source, R.id.rrComplaint, "field 'rrComplaint'", RecyclerView.class);
    target.tvEmpty = Utils.findRequiredViewAsType(source, R.id.tvEmpty, "field 'tvEmpty'", TextView.class);
    target.tvDisconnected = Utils.findRequiredViewAsType(source, R.id.tvDisconnected, "field 'tvDisconnected'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivExit = null;
    target.tvName = null;
    target.tvPosition = null;
    target.rrComplaint = null;
    target.tvEmpty = null;
    target.tvDisconnected = null;
  }
}
