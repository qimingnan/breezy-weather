package wangdaye.com.geometricweather.main.ui.adapter.main.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.basic.GeoActivity;
import wangdaye.com.geometricweather.basic.model.location.Location;
import wangdaye.com.geometricweather.basic.model.weather.Weather;
import wangdaye.com.geometricweather.main.ui.MainColorPicker;
import wangdaye.com.geometricweather.main.ui.adapter.daily.DailyAirQualityAdapter;
import wangdaye.com.geometricweather.main.ui.adapter.daily.DailyTemperatureAdapter;
import wangdaye.com.geometricweather.main.ui.adapter.daily.DailyUVAdapter;
import wangdaye.com.geometricweather.main.ui.adapter.daily.DailyWindAdapter;
import wangdaye.com.geometricweather.main.ui.adapter.main.MainTag;
import wangdaye.com.geometricweather.resource.provider.ResourceProvider;
import wangdaye.com.geometricweather.settings.SettingsOptionManager;
import wangdaye.com.geometricweather.ui.adapter.TagAdapter;
import wangdaye.com.geometricweather.ui.decotarion.GridMarginsDecoration;
import wangdaye.com.geometricweather.ui.widget.trend.TrendRecyclerView;
import wangdaye.com.geometricweather.ui.widget.weatherView.WeatherView;
import wangdaye.com.geometricweather.utils.DisplayUtils;

public class DailyViewHolder extends AbstractMainViewHolder {

    private CardView card;

    private TextView title;
    private TextView subtitle;
    private RecyclerView tagView;
    private TrendRecyclerView trendRecyclerView;
    
    @NonNull private WeatherView weatherView;
    @Px private float cardMarginsVertical;
    @Px private float cardMarginsHorizontal;

    public DailyViewHolder(@NonNull Activity activity, ViewGroup parent, @NonNull WeatherView weatherView,
                           @NonNull ResourceProvider provider, @NonNull MainColorPicker picker,
                           @Px float cardMarginsVertical, @Px float cardMarginsHorizontal,
                           @Px float cardRadius, @Px float cardElevation) {
        super(activity,
                LayoutInflater.from(activity).inflate(R.layout.container_main_daily_trend_card, parent, false),
                provider, picker, cardMarginsVertical, cardMarginsHorizontal, cardRadius, cardElevation, false);

        this.card = itemView.findViewById(R.id.container_main_daily_trend_card);
        this.title = itemView.findViewById(R.id.container_main_daily_trend_card_title);
        this.subtitle = itemView.findViewById(R.id.container_main_daily_trend_card_subtitle);
        this.tagView = itemView.findViewById(R.id.container_main_daily_trend_card_tagView);
        this.trendRecyclerView = itemView.findViewById(R.id.container_main_daily_trend_card_trendRecyclerView);
        
        this.weatherView = weatherView;
        this.cardMarginsVertical = cardMarginsVertical;
        this.cardMarginsHorizontal = cardMarginsHorizontal;
    }

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public void onBindView(@NonNull Location location) {
        Weather weather = location.getWeather();
        assert weather != null;

        card.setCardBackgroundColor(picker.getRootColor(context));

        title.setTextColor(weatherView.getThemeColors(picker.isLightTheme())[0]);

        if (TextUtils.isEmpty(weather.getCurrent().getDailyForecast())) {
            subtitle.setVisibility(View.GONE);
        } else {
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(weather.getCurrent().getDailyForecast());
        }

        List<TagAdapter.Tag> tagList = new ArrayList<>();
        tagList.add(new MainTag(context.getString(R.string.tag_temperature), MainTag.Type.TEMPERATURE));
        tagList.add(new MainTag(context.getString(R.string.tag_wind), MainTag.Type.WIND));
        tagList.add(new MainTag(context.getString(R.string.tag_aqi), MainTag.Type.AIR_QUALITY));
        tagList.add(new MainTag(context.getString(R.string.tag_uv), MainTag.Type.UV_INDEX));

        if (tagList.size() == 0) {
            tagView.setVisibility(View.GONE);
        } else {
            tagView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            tagView.addItemDecoration(
                    new GridMarginsDecoration(
                            context.getResources().getDimension(R.dimen.little_margin),
                            context.getResources().getDimension(R.dimen.normal_margin),
                            RecyclerView.HORIZONTAL
                    )
            );
            tagView.setAdapter(
                    new TagAdapter(tagList, (checked, oldPosition, newPosition) -> {
                        setTrendAdapterByTag(weather, (MainTag) tagList.get(newPosition));
                        return false;
                    }, 0)
            );
        }

        trendRecyclerView.setHasFixedSize(true);
        trendRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        setTrendAdapterByTag(weather, (MainTag) tagList.get(0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trendRecyclerView.setAdapter(null);
    }

    private void setTrendAdapterByTag(Weather weather, MainTag tag) {
        switch (tag.getType()) {
            case TEMPERATURE:
                trendRecyclerView.setAdapter(
                        new DailyTemperatureAdapter(
                                (GeoActivity) context, trendRecyclerView,
                                cardMarginsVertical, cardMarginsHorizontal,
                                DisplayUtils.isTabletDevice(context) ? 7 : 5,
                                context.getResources().getDimensionPixelSize(R.dimen.daily_trend_item_height),
                                weather,
                                weatherView.getThemeColors(picker.isLightTheme()),
                                provider,
                                picker,
                                SettingsOptionManager.getInstance(context).getTemperatureUnit()
                        )
                );
                break;

            case WIND:
                trendRecyclerView.setAdapter(
                        new DailyWindAdapter(
                                (GeoActivity) context, trendRecyclerView,
                                cardMarginsVertical, cardMarginsHorizontal,
                                DisplayUtils.isTabletDevice(context) ? 7 : 5,
                                context.getResources().getDimensionPixelSize(R.dimen.daily_trend_item_height),
                                weather,
                                weatherView.getThemeColors(picker.isLightTheme()),
                                picker,
                                SettingsOptionManager.getInstance(context).getSpeedUnit()
                        )
                );
                break;

            case AIR_QUALITY:
                trendRecyclerView.setAdapter(
                        new DailyAirQualityAdapter(
                                (GeoActivity) context, trendRecyclerView,
                                cardMarginsVertical, cardMarginsHorizontal,
                                DisplayUtils.isTabletDevice(context) ? 7 : 5,
                                context.getResources().getDimensionPixelSize(R.dimen.daily_trend_item_height),
                                weather,
                                weatherView.getThemeColors(picker.isLightTheme()),
                                provider,
                                picker
                        )
                );
                break;

            case UV_INDEX:
                trendRecyclerView.setAdapter(
                        new DailyUVAdapter(
                                (GeoActivity) context, trendRecyclerView,
                                cardMarginsVertical, cardMarginsHorizontal,
                                DisplayUtils.isTabletDevice(context) ? 7 : 5,
                                context.getResources().getDimensionPixelSize(R.dimen.daily_trend_item_height),
                                weather,
                                weatherView.getThemeColors(picker.isLightTheme()),
                                provider,
                                picker
                        )
                );
                break;
        }
    }
}