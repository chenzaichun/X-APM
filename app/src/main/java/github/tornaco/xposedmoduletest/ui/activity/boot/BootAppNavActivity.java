package github.tornaco.xposedmoduletest.ui.activity.boot;

import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import github.tornaco.android.common.Collections;
import github.tornaco.android.common.Consumer;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.loader.BootPackageLoader;
import github.tornaco.xposedmoduletest.model.CommonPackageInfo;
import github.tornaco.xposedmoduletest.ui.activity.BlockRecordViewerActivity;
import github.tornaco.xposedmoduletest.ui.activity.common.CommonPackageInfoListActivity;
import github.tornaco.xposedmoduletest.ui.adapter.common.CommonPackageInfoAdapter;
import github.tornaco.xposedmoduletest.ui.widget.SwitchBar;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;

public class BootAppNavActivity extends CommonPackageInfoListActivity implements SwitchBar.OnSwitchChangeListener {

    @Override
    protected void onRequestClearItemsInBackground() {
        Collections.consumeRemaining(getCommonPackageInfoAdapter().getCommonPackageInfos(),
                new Consumer<CommonPackageInfo>() {
                    @Override
                    public void accept(CommonPackageInfo commonPackageInfo) {
                        if (commonPackageInfo.isChecked()) {
                            XAshmanManager.get().addOrRemoveBootBlockApps(new String[]{commonPackageInfo.getPkgName()},
                                    XAshmanManager.Op.REMOVE);
                        }
                    }
                });
    }

    @Override
    protected void onInitSwitchBar(SwitchBar switchBar) {
        switchBar.show();
        switchBar.setChecked(XAshmanManager.get().isBlockBlockEnabled());
        switchBar.addOnSwitchChangeListener(this);
    }

    @Override
    protected void onRequestPick() {
        BootAppPickerActivity.start(getActivity());
    }

    @Override
    protected int getSummaryRes() {
        return R.string.summary_boot_app;
    }

    @Override
    protected CommonPackageInfoAdapter onCreateAdapter() {
        return new CommonPackageInfoAdapter(this);
    }

    @Override
    protected List<CommonPackageInfo> performLoading() {
        return BootPackageLoader.Impl.create(this).loadInstalled(true);
    }

    @Override
    public void onSwitchChanged(SwitchCompat switchView, boolean isChecked) {
        XAshmanManager.get().setBootBlockEnabled(isChecked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boot_block, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_block_record_viewer) {
            BlockRecordViewerActivity.start(this, null);
        }
        return super.onOptionsItemSelected(item);
    }
}