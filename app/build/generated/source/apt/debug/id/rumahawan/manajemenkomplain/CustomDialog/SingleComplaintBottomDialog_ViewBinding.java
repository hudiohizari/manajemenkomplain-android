// Generated code from Butter Knife. Do not modify!
package id.rumahawan.manajemenkomplain.CustomDialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import id.rumahawan.manajemenkomplain.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SingleComplaintBottomDialog_ViewBinding implements Unbinder {
  private SingleComplaintBottomDialog target;

  @UiThread
  public SingleComplaintBottomDialog_ViewBinding(SingleComplaintBottomDialog target, View source) {
    this.target = target;

    target.clBackground = Utils.findRequiredViewAsType(source, R.id.clBackground, "field 'clBackground'", NestedScrollView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    target.ivClose = Utils.findRequiredViewAsType(source, R.id.ivClose, "field 'ivClose'", ImageView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tvName, "field 'tvName'", TextView.class);
    target.ivStatus = Utils.findRequiredViewAsType(source, R.id.ivStatus, "field 'ivStatus'", ImageView.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tvTitle, "field 'tvTitle'", TextView.class);
    target.tvDescription = Utils.findRequiredViewAsType(source, R.id.tvDescription, "field 'tvDescription'", TextView.class);
    target.ivViewImageDescription = Utils.findRequiredViewAsType(source, R.id.ivViewImageDescription, "field 'ivViewImageDescription'", ImageView.class);
    target.etResponse = Utils.findRequiredViewAsType(source, R.id.etResponse, "field 'etResponse'", EditText.class);
    target.tvResponseLabel = Utils.findRequiredViewAsType(source, R.id.tvResponseLabel, "field 'tvResponseLabel'", TextView.class);
    target.tvResponseAnswered = Utils.findRequiredViewAsType(source, R.id.tvResponseAnswered, "field 'tvResponseAnswered'", TextView.class);
    target.tvResponseLabelAnswered = Utils.findRequiredViewAsType(source, R.id.tvResponseLabelAnswered, "field 'tvResponseLabelAnswered'", TextView.class);
    target.ivViewImage = Utils.findRequiredViewAsType(source, R.id.ivViewImage, "field 'ivViewImage'", ImageView.class);
    target.tvGambarLabel = Utils.findRequiredViewAsType(source, R.id.tvGambarLabel, "field 'tvGambarLabel'", TextView.class);
    target.vAddImage = Utils.findRequiredView(source, R.id.vAddImage, "field 'vAddImage'");
    target.ivAddImage = Utils.findRequiredViewAsType(source, R.id.ivAddImage, "field 'ivAddImage'", ImageView.class);
    target.ivAddedImage = Utils.findRequiredViewAsType(source, R.id.ivAddedImage, "field 'ivAddedImage'", ImageView.class);
    target.tvGambarOptional = Utils.findRequiredViewAsType(source, R.id.tvGambarOptional, "field 'tvGambarOptional'", TextView.class);
    target.btnSubmit = Utils.findRequiredViewAsType(source, R.id.btnSubmit, "field 'btnSubmit'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SingleComplaintBottomDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.clBackground = null;
    target.progressBar = null;
    target.ivClose = null;
    target.tvName = null;
    target.ivStatus = null;
    target.tvTitle = null;
    target.tvDescription = null;
    target.ivViewImageDescription = null;
    target.etResponse = null;
    target.tvResponseLabel = null;
    target.tvResponseAnswered = null;
    target.tvResponseLabelAnswered = null;
    target.ivViewImage = null;
    target.tvGambarLabel = null;
    target.vAddImage = null;
    target.ivAddImage = null;
    target.ivAddedImage = null;
    target.tvGambarOptional = null;
    target.btnSubmit = null;
  }
}
