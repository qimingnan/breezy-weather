package wangdaye.com.geometricweather.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import wangdaye.com.geometricweather.common.utils.managers.ThemeManager;
import wangdaye.com.geometricweather.common.utils.managers.TimeManager;
import wangdaye.com.geometricweather.db.DatabaseHelper;
import wangdaye.com.geometricweather.settings.SettingsOptionManager;

@InstallIn(ApplicationComponent.class)
@Module
public class UtilsModule {

    @Provides
    public DatabaseHelper provideDatabaseHelper(@ApplicationContext Context context) {
        return DatabaseHelper.getInstance(context);
    }

    @Provides
    public SettingsOptionManager provideSettingsOptionManager(@ApplicationContext Context context) {
        return SettingsOptionManager.getInstance(context);
    }

    @Provides
    public ThemeManager provideThemeManager(@ApplicationContext Context context) {
        return ThemeManager.getInstance(context);
    }

    @Provides
    public TimeManager provideTimeManager(@ApplicationContext Context context) {
        return TimeManager.getInstance(context);
    }
}