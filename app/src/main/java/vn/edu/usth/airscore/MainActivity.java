package vn.edu.usth.airscore;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import android.widget.LinearLayout;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    // Các TextView hiển thị thông tin
    private TextView cityNameText, temperatureText, bigDustText, smallDustText, ozoneText, carbonText, nitroText, sulfurText, humidityText, AQIText;
    private Button refreshButton;
    private EditText cityNameInput;


    // Các LinearLayout để thay đổi màu nền
    private LinearLayout bigDustLayout, dustLayout, ozonLayout, carbonLayout, nitroLayout, sulfurLayout, humidityLayout, AQILayout;


    // Token API
    private static final String API_TOKEN = "08ae6daebe6eba197a3b5b33944c1e099a25bcd5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết layout
        cityNameText    = findViewById(R.id.cityNameText);
        temperatureText = findViewById(R.id.temperatureText);
        bigDustText     = findViewById(R.id.bigDustText);
        smallDustText   = findViewById(R.id.smallDustText);
        ozoneText       = findViewById(R.id.ozoneText);
        carbonText      = findViewById(R.id.carbonText);
        nitroText       = findViewById(R.id.nitroText);
        sulfurText      = findViewById(R.id.sulfurText);
        humidityText    = findViewById(R.id.humidityText);
        AQIText         = findViewById(R.id.AQIText);
        refreshButton   = findViewById(R.id.fetchButton);
        cityNameInput   = findViewById(R.id.cityNameInput);


        // Liên kết các layout card
        bigDustLayout   = findViewById(R.id.bigDustLayout);
        dustLayout      = findViewById(R.id.dustLayout);
        ozonLayout      = findViewById(R.id.ozonLayout);
        carbonLayout    = findViewById(R.id.carbonLayout);
        nitroLayout     = findViewById(R.id.nitroLayout);
        sulfurLayout    = findViewById(R.id.sulfurLayout);
        humidityLayout  = findViewById(R.id.humidityLayout);
        AQILayout       = findViewById(R.id.AQILayout);


        //  Gán sẵn tên thành phố
        cityNameInput.setText("Hanoi");
        cityNameText.setText("Hanoi");

        // Khi nhấn nút "Làm mới"
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityNameInput.getText().toString();
                if (!cityName.isEmpty()) {
                    fetchWeatherData(cityName);
                } else {
                    cityNameInput.setError("Vui lòng nhập tên thành phố");
                }
            }
        });

        // Tải mặc định cho Hà Nội
        fetchWeatherData("Hanoi");
    }

    /**
     * Gửi yêu cầu HTTP đến API WAQI để lấy dữ liệu chất lượng không khí.
     */
    private void fetchWeatherData(String cityName) {
        String url = "https://api.waqi.info/feed/" + cityName + "/?token=" + API_TOKEN;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();

                runOnUiThread(() -> updateUI(result));

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Không thể kết nối API!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * Cập nhật giao diện từ dữ liệu JSON nhận được từ API.
     */
    private void updateUI(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("status").equals("ok")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject iaqi = data.getJSONObject("iaqi");

                    // Lấy dữ liệu
                    double temperature = iaqi.has("t") ? iaqi.getJSONObject("t").getDouble("v") : 0;
                    double pm10 = iaqi.has("pm10") ? iaqi.getJSONObject("pm10").getDouble("v") : 0;
                    double pm25 = iaqi.has("pm25") ? iaqi.getJSONObject("pm25").getDouble("v") : 0;
                    double ozone = iaqi.has("o3") ? iaqi.getJSONObject("o3").getDouble("v") : 0;
                    double nitrogen = iaqi.has("no2") ? iaqi.getJSONObject("no2").getDouble("v") : 0;
                    double sulfur = iaqi.has("so2") ? iaqi.getJSONObject("so2").getDouble("v") : 0;
                    double carbon = iaqi.has("co") ? iaqi.getJSONObject("co").getDouble("v") : 0;
                    double humidity = iaqi.has("h") ? iaqi.getJSONObject("h").getDouble("v") : 0;
                    int aqi = data.getInt("aqi");

                    // Cập nhật giao diện
                    cityNameText.setText(cityNameInput.getText().toString());
                    temperatureText.setText(String.format("%.0f°C", temperature));
                    bigDustText.setText(String.format("%.0f", pm10));
                    smallDustText.setText(String.format("%.0f", pm25));
                    ozoneText.setText(String.format("%.0f", ozone));
                    nitroText.setText(String.format("%.0f", nitrogen));
                    sulfurText.setText(String.format("%.0f", sulfur));
                    carbonText.setText(String.format("%.1f", carbon));
                    humidityText.setText(String.format("%.0f", humidity));
                    AQIText.setText(String.format("%d", aqi));


                    // Cập nhật màu sắc cho các card
                    AQILayout.setBackgroundColor(ContextCompat.getColor(this, getAqiColor(aqi)));
                    dustLayout.setBackgroundColor(ContextCompat.getColor(this, getPm25Color(pm25)));
                    bigDustLayout.setBackgroundColor(ContextCompat.getColor(this, getPm10Color(pm10)));
                    ozonLayout.setBackgroundColor(ContextCompat.getColor(this, getOzoneColor(ozone)));
                    carbonLayout.setBackgroundColor(ContextCompat.getColor(this, getCoColor(carbon)));
                    sulfurLayout.setBackgroundColor(ContextCompat.getColor(this, getSo2Color(sulfur)));
                    nitroLayout.setBackgroundColor(ContextCompat.getColor(this, getNo2Color(nitrogen)));
                    humidityLayout.setBackgroundColor(ContextCompat.getColor(this, getHumidityColor(humidity)));


                } else {
                    Toast.makeText(this, "Không tìm thấy dữ liệu cho thành phố này", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi đọc dữ liệu JSON!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Các hàm helper để lấy màu dựa trên chỉ số ô nhiễm.
     * Thang đo được tham khảo từ ảnh image_862665.png do người dùng cung cấp.
     */
    private int getAqiColor(int aqi) {
        if (aqi <= 50) return R.color.good;
        if (aqi <= 100) return R.color.moderate;
        if (aqi <= 150) return R.color.unhealthy_sensitive;
        if (aqi <= 200) return R.color.unhealthy;
        if (aqi <= 300) return R.color.very_unhealthy;
        return R.color.hazardous;
    }

    private int getPm25Color(double pm25) {
        if (pm25 <= 12.0) return R.color.good;
        if (pm25 <= 35.4) return R.color.moderate;
        if (pm25 <= 55.4) return R.color.unhealthy_sensitive;
        if (pm25 <= 150.4) return R.color.unhealthy;
        if (pm25 <= 250.4) return R.color.very_unhealthy;
        return R.color.hazardous;
    }


    private int getPm10Color(double pm10) {
        if (pm10 <= 54) return R.color.good;
        if (pm10 <= 154) return R.color.moderate;
        if (pm10 <= 254) return R.color.unhealthy_sensitive;
        if (pm10 <= 354) return R.color.unhealthy;
        if (pm10 <= 424) return R.color.very_unhealthy;
        return R.color.hazardous;
    }


    private int getOzoneColor(double ozone_ppb) {
        if (ozone_ppb <= 54) return R.color.good;
        if (ozone_ppb <= 70) return R.color.moderate;
        if (ozone_ppb <= 85) return R.color.unhealthy_sensitive;
        if (ozone_ppb <= 105) return R.color.unhealthy;
        if (ozone_ppb <= 200) return R.color.very_unhealthy;
        return R.color.hazardous;
    }

    private int getCoColor(double co_ppm) {
        if (co_ppm <= 4.4) return R.color.good;
        if (co_ppm <= 9.4) return R.color.moderate;
        if (co_ppm <= 12.4) return R.color.unhealthy_sensitive;
        if (co_ppm <= 15.4) return R.color.unhealthy;
        if (co_ppm <= 30.4) return R.color.very_unhealthy;
        return R.color.hazardous;
    }


    private int getSo2Color(double so2_ppb) {
        if (so2_ppb <= 35) return R.color.good;
        if (so2_ppb <= 75) return R.color.moderate;
        if (so2_ppb <= 185) return R.color.unhealthy_sensitive;
        if (so2_ppb <= 304) return R.color.unhealthy;
        if (so2_ppb <= 604) return R.color.very_unhealthy;
        return R.color.hazardous;
    }

    private int getNo2Color(double no2_value) {

        if (no2_value <= 53) return R.color.good;
        if (no2_value <= 100) return R.color.moderate;
        if (no2_value <= 360) return R.color.unhealthy_sensitive;
        if (no2_value <= 649) return R.color.unhealthy;
        if (no2_value <= 1249) return R.color.very_unhealthy;
        return R.color.hazardous;
    }


    private int getHumidityColor(double humidity) {
        if (humidity < 25) return R.color.lv1human;
        if (humidity <= 30) return R.color.lv2human;
        if (humidity <= 60) return R.color.lv3human;
        if (humidity > 60) return R.color.lv4human;

        return R.color.unhealthy_sensitive;
    }

}
