package com.schafer.sys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class WiFiActivity extends AppCompatActivity implements Module {
    private LinearLayout wifiLL;

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_wifi);
        setTitle("Wi-Fi Information");

        wifiLL = findViewById(R.id.wifi_info_LL);
        getDevicesInfo();
    }

    private void getDevicesInfo() {

        // WiFi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        Vector<Metric> wm = new Vector<>();

        String title = "";
        if (wifiInfo.getHiddenSSID()) {
            title = "Hidden Network";
        } else if (Objects.equals(wifiInfo.getSSID(), WifiManager.UNKNOWN_SSID)) {
            title = "Current Network";
        } else {
            title = wifiInfo.getSSID().replaceAll("\"", "");
        }

        int wifiTableID = generateCategory(wifiLL, title);

        wm.add(new Metric("WiFi state", getWifiStatus(wifiManager.getWifiState())));
        wm.add(new Metric("WiFi network ID", wifiInfo.getNetworkId()));
        wm.add(new Metric("WiFi BSSID", String.valueOf(wifiInfo.getBSSID()), "02:00:00:00:00:00", "INSUFFICIENT_PERMISSION", "<data>"));
        wm.add(new Metric("WiFi SSID", wifiInfo.getSSID()));
        wm.add(new Metric("hidden SSID", wifiInfo.getHiddenSSID()));
        wm.add(new Metric("client MAC", wifiInfo.getMacAddress(), "02:00:00:00:00:00", "INSUFFICIENT_PERMISSION", "<data>"));
        wm.add(new Metric("WiFi standard", getWifiStandard(wifiInfo.getWifiStandard())));
        wm.add(new Metric("security type", getSecurityType(wifiInfo.getCurrentSecurityType())));

        wm.add(new Metric("RSSI", wifiInfo.getRssi() + " dbm"));
        wm.add(new Metric("frequency", wifiInfo.getFrequency() + " Mhz"));
        wm.add(new Metric("link speed", wifiInfo.getLinkSpeed() + " Mbps"));
        wm.add(new Metric("rx max speed", wifiInfo.getMaxSupportedRxLinkSpeedMbps() + " Mbps"));
        wm.add(new Metric("tx max speed", wifiInfo.getMaxSupportedTxLinkSpeedMbps() + " Mbps"));
        wm.add(new Metric("rx link speed", wifiInfo.getRxLinkSpeedMbps() + " Mbps"));
        wm.add(new Metric("tx link speed", wifiInfo.getTxLinkSpeedMbps() + " Mbps"));

        wm.add(new Metric("STA MLD", String.valueOf(wifiInfo.getAffiliatedMloLinks())));
        wm.add(new Metric("MLD-AP MAC", String.valueOf(wifiInfo.getApMldMacAddress())));
        wm.add(new Metric("MLO-AP link-ID", wifiInfo.getApMloLinkId()));
        wm.add(new Metric("redactions", wifiInfo.getApplicableRedactions()));
        wm.add(new Metric("MLO links", String.valueOf(wifiInfo.getAssociatedMloLinks())));
        //wm.add(new Metric("", String.valueOf(wifiInfo.getClass())));
        //wm.add(new Metric("", String.valueOf(wifiInfo.getInformationElements()))); ///// IDK WHAT THIS DOES LMAO IDC
        //wm.add(new Metric("IP address", wifiInfo.getIpAddress())); //no longer works
        wm.add(new Metric("passpoint FQDN", wifiInfo.getPasspointFqdn()));
        wm.add(new Metric("passpoint name", wifiInfo.getPasspointProviderFriendlyName()));
        wm.add(new Metric("subscription ID", wifiInfo.getSubscriptionId()));
        wm.add(new Metric("AP supplicant", String.valueOf(wifiInfo.getSupplicantState())));
        wm.add(new Metric("hash code", wifiInfo.hashCode()));
        wm.add(new Metric("restricted", wifiInfo.isRestricted()));

        //wm.add(new Metric("", wifiManager.getAllowedChannels()));

        // THIS CAN ONLY BE CALLED BY SYSTEM APPS
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
        //    wm.add(new Metric("", String.valueOf(wifiManager.getCallerConfiguredNetworks())));
        //}

        //wm.add(new Metric("", wifiManager.getChannelData();));
        //wm.add(new Metric("", wifiManager.getConfiguredNetworks()));
        wm.add(new Metric("connection info", String.valueOf(wifiManager.getConnectionInfo())));
        wm.add(new Metric("DHCP info", String.valueOf(wifiManager.getDhcpInfo())));

        displayMetrics(findViewById(wifiTableID), wm);

        int wifiHardwareID = generateCategory(wifiLL, "Hardware Capabilities");
        Vector<Metric> wh = new Vector<>();


        wh.add(new Metric("max channels/req", wifiManager.getMaxNumberOfChannelsPerNetworkSpecifierRequest()));
        wh.add(new Metric("max suggestions/app", wifiManager.getMaxNumberOfNetworkSuggestionsPerApp()));
        wh.add(new Metric("max signal level", wifiManager.getMaxSignalLevel()));
        //wh.add(new Metric("", wifiManager.getMaxSupportedConcurrentTdlsSessions();));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wh.add(new Metric("network suggestions", String.valueOf(wifiManager.getNetworkSuggestions())));
            wh.add(new Metric("STA MIM", wifiManager.getStaConcurrencyForMultiInternetMode()));
        }


        wh.add(new Metric("wifi enabled", wifiManager.isWifiEnabled()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wh.add(new Metric("wifi passpoint enabled", wifiManager.isWifiPasspointEnabled()));
            wh.add(new Metric("auto-wakeup", wifiManager.isAutoWakeupEnabled()));
            wh.add(new Metric("scan throttling", wifiManager.isScanThrottleEnabled()));
        }

        wh.add(new Metric("5 GHz", wifiManager.is5GHzBandSupported(), "supported", "not supported"));
        wh.add(new Metric("6 GHz", wifiManager.is6GHzBandSupported(), "supported", "not supported"));
        wh.add(new Metric("2.4 GHz", wifiManager.is24GHzBandSupported(), "supported", "not supported"));
        wh.add(new Metric("60 GHz", wifiManager.is60GHzBandSupported(), "supported", "not supported"));
        wh.add(new Metric("bridged AP concurrency", wifiManager.isBridgedApConcurrencySupported(), "supported", "not supported"));
        wh.add(new Metric("decorated identity", wifiManager.isDecoratedIdentitySupported(), "supported", "not supported"));
        //wh.add(new Metric("", wifiManager.isDeviceToApRttSupported(), "supported", "not supported"));
        wh.add(new Metric("dual band simultaneous", wifiManager.isDualBandSimultaneousSupported(), "supported", "not supported"));
        wh.add(new Metric("easy connect DPP-AKM", wifiManager.isEasyConnectDppAkmSupported(), "supported", "not supported"));
        wh.add(new Metric("easy connect ERM", wifiManager.isEasyConnectEnrolleeResponderModeSupported(), "supported", "not supported"));
        wh.add(new Metric("easy connect", wifiManager.isEasyConnectSupported(), "supported", "not supported"));
        wh.add(new Metric("enhanced open", wifiManager.isEnhancedOpenSupported(), "supported", "not supported"));
        wh.add(new Metric("enhanced pwr reporting", wifiManager.isEnhancedPowerReportingSupported(), "supported", "not supported"));
        wh.add(new Metric("STA make-before-break", wifiManager.isMakeBeforeBreakWifiSwitchingSupported(), "supported", "not supported"));
        wh.add(new Metric("P2P", wifiManager.isP2pSupported(), "supported", "not supported"));
        wh.add(new Metric("wifi passpoint T&C", wifiManager.isPasspointTermsAndConditionsSupported(), "supported", "not supported"));
        wh.add(new Metric("preferred offload", wifiManager.isPreferredNetworkOffloadSupported(), "supported", "not supported"));
        //wh.add(new Metric("scan always available", wifiManager.isScanAlwaysAvailable(), "supported", "not supported"));
        wh.add(new Metric("STA+AP", wifiManager.isStaApConcurrencySupported(), "supported", "not supported"));
        wh.add(new Metric("STA+AP bridged", wifiManager.isStaBridgedApConcurrencySupported(), "supported", "not supported"));
        wh.add(new Metric("STA local", wifiManager.isStaConcurrencyForLocalOnlyConnectionsSupported(), "supported", "not supported"));
        wh.add(new Metric("STA multiline", wifiManager.isStaConcurrencyForMultiInternetSupported(), "supported", "not supported"));
        wh.add(new Metric("TDLS", wifiManager.isTdlsSupported(), "supported", "not supported"));
        wh.add(new Metric("TID-to-link-mapping", wifiManager.isTidToLinkMappingNegotiationSupported(), "supported", "not supported"));
        wh.add(new Metric("TLS minimum version", wifiManager.isTlsMinimumVersionSupported(), "supported", "not supported"));
        wh.add(new Metric("TLS v1.3", wifiManager.isTlsV13Supported(), "supported", "not supported"));
        wh.add(new Metric("trust-on-first-use", wifiManager.isTrustOnFirstUseSupported(), "supported", "not supported"));
        wh.add(new Metric("wapi", wifiManager.isWapiSupported(), "supported", "not supported"));
        wh.add(new Metric("wifi display r2", wifiManager.isWifiDisplayR2Supported(), "supported", "not supported"));

        wh.add(new Metric("legacy 802.11", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_LEGACY), "supported", "not supported"));
        wh.add(new Metric("802.11n (Wi-Fi 4)", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_11N), "supported", "not supported"));
        wh.add(new Metric("802.11ac (Wi-Fi 5)", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_11AC), "supported", "not supported"));
        wh.add(new Metric("802.11ax (Wi-Fi 6)", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_11AX), "supported", "not supported"));
        wh.add(new Metric("802.11ad (WiGig)", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_11AD), "supported", "not supported"));
        wh.add(new Metric("802.11be (Wi-Fi 7)", wifiManager.isWifiStandardSupported(ScanResult.WIFI_STANDARD_11BE), "supported", "not supported"));
        wh.add(new Metric("WPA3 SAE-H2E", wifiManager.isWpa3SaeH2eSupported(), "supported", "not supported"));
        wh.add(new Metric("WPA3 SAE-PSK", wifiManager.isWpa3SaePublicKeySupported(), "supported", "not supported"));
        wh.add(new Metric("WPA3 SAE", wifiManager.isWpa3SaeSupported(), "supported", "not supported"));
        wh.add(new Metric("WPA3 Suite-B", wifiManager.isWpa3SuiteBSupported(), "supported", "not supported"));


        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED) {
        //    // TODO: Consider calling
        //    //    ActivityCompat#requestPermissions
        //    // here to request the missing permissions, and then overriding
        //    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //    //                                          int[] grantResults)
        //    // to handle the case where the user grants the permission. See the documentation
        //    // for ActivityCompat#requestPermissions for more details.
        //    return;

        //    wh.add(new Metric("", wifiManager.getUsableChannels()));

        //}

        displayMetrics(findViewById(wifiHardwareID), wh);

        // Saved Networks
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //wm.add(new Metric("", String.valueOf(wifiManager.getScanResults())));

            int wifiScanID = generateCategory(wifiLL, "Nearby Networks");
            Vector<Metric> ws = new Vector<>();
            List<ScanResult> results = wifiManager.getScanResults();
            for (ScanResult result : results) {
                String ssid = String.valueOf(result.getWifiSsid());
                if (ssid.isEmpty()) {ssid = "Hidden Network";}
                ssid = ssid.replaceAll("\"", "");
                ws.add(new Metric("<subhead>", ssid));
                ws.add(new Metric("SSID", String.valueOf(result.getWifiSsid())));
                ws.add(new Metric("BSSID", result.BSSID));
                long millisSinceBoot = result.timestamp;
                long days = TimeUnit.MILLISECONDS.toDays(millisSinceBoot);
                long hours = TimeUnit.MILLISECONDS.toHours(millisSinceBoot) - TimeUnit.DAYS.toHours(days);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisSinceBoot) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisSinceBoot) - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
                String timeSinceBoot = String.format("%d days %02d:%02d:%02d", days, hours, minutes, seconds);
                ws.add(new Metric("last seen", timeSinceBoot));
                ws.add(new Metric("Wi-Fi standard", getWifiStandard(result.getWifiStandard())));
                ws.add(new Metric("security types", getSecurityTypesString(result))); ////////
                ws.add(new Metric("capabilities", result.capabilities));
                ws.add(new Metric("frequency", result.frequency + " Mhz"));
                ws.add(new Metric("RSSI", result.level + " dbm"));
                ws.add(new Metric("center freq0", result.centerFreq0 + " Mhz"));
                ws.add(new Metric("center freq1", result.centerFreq1 + " Mhz"));
                ws.add(new Metric("channel width", result.channelWidth + " Mhz"));
                ws.add(new Metric("contents", result.describeContents()));
                ws.add(new Metric("MLO links", String.valueOf(result.getAffiliatedMloLinks())));
                ws.add(new Metric("MLD MAC", String.valueOf(result.getApMldMacAddress())));
                ws.add(new Metric("MLO link ID", result.getApMloLinkId()));
                //ws.add(new Metric("\tinfo elements", String.valueOf(result.getInformationElements())));
                ws.add(new Metric("hash code", result.hashCode()));
                ws.add(new Metric("802.11mc", result.is80211mcResponder()));
                ws.add(new Metric("passpoint", result.isPasspointNetwork()));


            }
            //Doesn't work after android 10 - get saved networks
            //List<WifiConfiguration> savedNetworks = wifiManager.getConfiguredNetworks();
            //for (WifiConfiguration network : savedNetworks) {
            //    wm.add(new Metric("Saved Network SSID", network.SSID));
            //    wm.add(new Metric("Saved Network BSSID", network.BSSID));
            //}

            displayMetrics(findViewById(wifiScanID), ws);

        }
    }

    private String getWifiStandard(int wifi_standard) {
        switch (wifi_standard) {
            case ScanResult.WIFI_STANDARD_UNKNOWN:
                return "unknown";
            case ScanResult.WIFI_STANDARD_LEGACY:
                return "legacy";
            case ScanResult.WIFI_STANDARD_11N:
                return "802.11n";
            case ScanResult.WIFI_STANDARD_11AC:
                return "802.11ac";
            case ScanResult.WIFI_STANDARD_11AX:
                return "802.11ax";
            case ScanResult.WIFI_STANDARD_11AD:
                return "802.11ad";
            case ScanResult.WIFI_STANDARD_11BE:
                return "802.11be";
            default:
                return "--error: unknown Wi-Fi standard ID--";
        }
    }

    private String getWifiStatus(int wifi_status) {
        switch (wifi_status) {
            case WifiManager.WIFI_STATE_DISABLING:
                return "disabling";
            case WifiManager.WIFI_STATE_DISABLED:
                return "disabled";
            case WifiManager.WIFI_STATE_ENABLING:
                return "enabling";
            case WifiManager.WIFI_STATE_ENABLED:
                return "enabled";
            case WifiManager.WIFI_STATE_UNKNOWN:
                return "unknown";
            default:
                return "--error: unknown Wi-Fi state ID--";
        }
    }

    private String getSecurityTypesString(ScanResult result) {
        int[] securityTypes = result.getSecurityTypes();
        StringBuilder sb = new StringBuilder();
        for (int type : securityTypes) {
           sb.append(getSecurityType(type)).append(", ");
        }
        // Remove the last comma and space
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private String getSecurityType(int type) {
        switch (type) {
            case WifiInfo.SECURITY_TYPE_UNKNOWN:
                return("UNKNOWN");
            case WifiInfo.SECURITY_TYPE_OPEN:
                return("OPEN");
            case WifiInfo.SECURITY_TYPE_WEP:
                return("WEP");
            case WifiInfo.SECURITY_TYPE_PSK:
                return("PSK");
            case WifiInfo.SECURITY_TYPE_EAP:
                return("EAP");
            case WifiInfo.SECURITY_TYPE_SAE:
                return("SAE");
            case WifiInfo.SECURITY_TYPE_OWE:
                return("OWE");
            case WifiInfo.SECURITY_TYPE_WAPI_PSK:
                return("WAPI_PSK");
            case WifiInfo.SECURITY_TYPE_WAPI_CERT:
                return("WAPI_CERT");
            case WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE:
                return("EAP_WPA3_ENTERPRISE");
            case WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT:
                return("EAP_WPA3_ENTERPRISE_192_BIT");
            case WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2:
                return("PASSPOINT_R1_R2");
            case WifiInfo.SECURITY_TYPE_PASSPOINT_R3:
                return("PASSPOINT_R3");
            case WifiInfo.SECURITY_TYPE_DPP:
                return("DPP");
            default:
                return("--error: unknown wifi security code--");
        }
    }



}
