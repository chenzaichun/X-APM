package github.tornaco.xposedmoduletest.ui.activity.rf;

import android.support.v7.widget.SwitchCompat;
import android.view.Menu;

import java.util.List;

import github.tornaco.android.common.Collections;
import github.tornaco.android.common.Consumer;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.loader.RFKillPackageLoader;
import github.tornaco.xposedmoduletest.model.CommonPackageInfo;
import github.tornaco.xposedmoduletest.ui.activity.common.CommonPackageInfoListActivity;
import github.tornaco.xposedmoduletest.ui.adapter.common.CommonPackageInfoAdapter;
import github.tornaco.xposedmoduletest.ui.widget.SwitchBar;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;

public class RFKillAppNavActivity extends CommonPackageInfoListActivity
        implements SwitchBar.OnSwitchChangeListener {

    @Override
    protected void onRequestClearItemsInBackground() {
        Collections.consumeRemaining(getCommonPackageInfoAdapter().getCommonPackageInfos(),
                new Consumer<CommonPackageInfo>() {
                    @Override
                    public void accept(CommonPackageInfo commonPackageInfo) {
                        if (commonPackageInfo.isChecked()) {
                            XAshmanManager.get().addOrRemoveRFKApps(new String[]{commonPackageInfo.getPkgName()},
                                    XAshmanManager.Op.REMOVE);
                        }
                    }
                });
    }

    @Override
    protected void onInitSwitchBar(SwitchBar switchBar) {
        switchBar.show();
        switchBar.setChecked(XAshmanManager.get().isRFKillEnabled());
        switchBar.addOnSwitchChangeListener(this);
    }

    @Override
    protected void onRequestPick() {
        RFKillAppPickerActivity.start(getActivity());
    }

    @Override
    protected int getSummaryRes() {
        return R.string.summary_rf_kill_app;
    }

    @Override
    protected CommonPackageInfoAdapter onCreateAdapter() {
        return new CommonPackageInfoAdapter(this);
    }

    @Override
    protected List<CommonPackageInfo> performLoading() {
        return RFKillPackageLoader.Impl.create(this).loadInstalled(true);
    }

    @Override
    public void onSwitchChanged(SwitchCompat switchView, boolean isChecked) {
        XAshmanManager.get().setRFKillEnabled(isChecked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rf_kill, menu);
        return super.onCreateOptionsMenu(menu);
    }
}