// Generated code from Butter Knife. Do not modify!
package id.rumahawan.manajemenkomplain.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import id.rumahawan.manajemenkomplain.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ComplaintListAdapter$ComplaintListViewHolder_ViewBinding implements Unbinder {
  private ComplaintListAdapter.ComplaintListViewHolder target;

  @UiThread
  public ComplaintListAdapter$ComplaintListViewHolder_ViewBinding(
      ComplaintListAdapter.ComplaintListViewHolder target, View source) {
    this.target = target;

    target.vBackground = Utils.findRequiredViewAsType(source, R.id.vBackground, "field 'vBackground'", ConstraintLayout.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tvName, "field 'tvName'", TextView.class);
    target.ivStatus = Utils.findRequiredViewAsType(source, R.id.ivStatus, "field 'ivStatus'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ComplaintListAdapter.ComplaintListViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.vBackground = null;
    target.tvName = null;
    target.ivStatus = null;
  }
}
