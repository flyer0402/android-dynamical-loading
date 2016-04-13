package me.kaede.mainapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.yy.mobile.ylink.dynamicload.fragment.DLBasePluginFragment;
import com.yy.mobile.ylink.dynamicload.fragment.PluginContextWrapper;
import tv.danmaku.pluginbehaiour.IToast;
import tv.danmaku.pluinlib.BasePluginHandler;
import tv.danmaku.pluinlib.BasePluginPackage;
import tv.danmaku.pluinlib.LogUtil;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	private BasePluginPackage basePluginPackage;
	private BasePluginHandler basePluginHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		YLink.init();
	}

	public void onClickLivePlugin(View view){
		Intent intent = new Intent(this, LivePluginActivity.class);
		startActivity(intent);
	}

	public void onLoadSimplePlugin(View view){
		// 测试 SimplePluginPackage
		File tempFile = new File( Environment.getExternalStorageDirectory() + File.separator + "pluginapp1-debug.apk");

		if (!tempFile.exists()) {
			LogUtil.w(TAG,"插件不存在");
			Toast.makeText(this,"插件不存在",Toast.LENGTH_LONG).show();
			return;
		}
		basePluginHandler = new BasePluginHandler(this);
		basePluginPackage = basePluginHandler.initPlugin(tempFile.getAbsolutePath());
		if (basePluginPackage==null){
			Toast.makeText(this,"加载插件失败",Toast.LENGTH_LONG).show();
		}

	}

	public void onCallMethod(View view){
		Class clazz = basePluginHandler.loadPluginClass(basePluginPackage,"me.kaede.pluginpackage.Entry");
		try {
			Method method = clazz.getMethod("getToast");
			IToast iToast = (IToast) method.invoke(null);
			iToast.toast(this,"dude!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
