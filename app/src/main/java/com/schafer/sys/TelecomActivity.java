package com.schafer.sys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.transition.Slide;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.Vector;

public class TelecomActivity extends AppCompatActivity implements Module {
    LinearLayout mainLL;

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_telecom);
        setTitle("Cellular Information");

        mainLL = findViewById(R.id.telecom_info_LL);
        getData();

    }

    private void getData() {
        int generalId = generateCategory(mainLL, "General");
        int simId = generateCategory(mainLL, "SIM");
        Vector<Metric> gm = new Vector<>(), sm = new Vector<>();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        gm.add(new Metric("[ apply to active/primary SIM ]", " "));

        gm.add(new Metric("SIM Carrier ID", tm.getSimCarrierId()));
        //tm.getImei();

        //tm.getDeviceId();
        gm.add(new Metric ("can change dial tone length",tm.canChangeDtmfToneLength()));

        gm.add(new Metric("active modem count", tm.getActiveModemCount()));
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //    tm.getCallComposerStatus()));
        //}

        gm.add(new Metric("SIM EUICC ID", tm.getCardIdForDefaultEuicc()));
        gm.add(new Metric("carrier ID from SIM MMC+MNC", tm.getCarrierIdFromSimMccMnc()));
        gm.add(new Metric ("data activity",DataStatus(tm.getDataActivity())));
        gm.add(new Metric ("data state",DataStatus(tm.getDataState())));

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //    tm.getManualNetworkSelectionPlmn()));
        //}
        gm.add(new Metric("manufacturer code", tm.getManufacturerCode()));
        //tm.getMeid()));
        gm.add(new Metric ("MMS address",tm.getMmsUAProfUrl()));
        gm.add(new Metric ("MMS user-agent",tm.getMmsUserAgent()));
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        //    tm.getNai()));
        //}
        gm.add(new Metric ("network country ISO",tm.getNetworkCountryIso()));
        gm.add(new Metric ("network operator",tm.getNetworkOperator()));
        gm.add(new Metric ("network operator name",tm.getNetworkOperatorName()));
        gm.add(new Metric ("network specifier",tm.getNetworkSpecifier()));

        //gm.add(new Metric ("Phone count",tm.getPhoneCount()));
        gm.add(new Metric ("phone type",getPhoneType(tm.getPhoneType())));

        //Deprecated
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        //    gm.add(new Metric ("Signal Strength", String.valueOf(tm.getSignalStrength())));
        //}
        sm.add(new Metric("carrier ID", tm.getSimCarrierId()));
        sm.add(new Metric("carrier ID name", String.valueOf(tm.getSimCarrierIdName())));
        sm.add(new Metric ("country ISO",tm.getSimCountryIso()));
        sm.add(new Metric ("operator",tm.getSimOperator()));
        sm.add(new Metric ("operator name",tm.getSimOperatorName()));
        //gm.add(new Metric ("LOLXDSOFINNYLOLROFL",tm.getSimSerialNumber()));
        sm.add(new Metric("SIM-specific carrier ID", tm.getSimSpecificCarrierId()));
        sm.add(new Metric("SIM-specific carrier ID name", String.valueOf(tm.getSimSpecificCarrierIdName())));
        sm.add(new Metric ("SIM state (SIM 1)",getSimState(tm.getSimState(0))));
        sm.add(new Metric ("SIM state (SIM 2)",getSimState(tm.getSimState(1))));
        //gm.add(new Metric ("LOLXDSOFINNYLOLROFL",tm.getSubscriberId()));
        gm.add(new Metric("subscription ID", tm.getSubscriptionId()));
        gm.add(new Metric("supported modem count", tm.getSupportedModemCount()));
        gm.add(new Metric("type allocation code", tm.getTypeAllocationCode()));
        gm.add(new Metric ("has carrier privileges",tm.hasCarrierPrivileges()));
        gm.add(new Metric ("ICC card present",tm.hasIccCard()));
        gm.add(new Metric ("concurrent voice+data support",tm.isConcurrentVoiceAndDataSupported()));
        gm.add(new Metric("data capable", tm.isDataCapable()));
        gm.add(new Metric ("hearing-aid compat. support",tm.isHearingAidCompatibilitySupported()));
        gm.add(new Metric ("network roaming",tm.isNetworkRoaming()));
        gm.add(new Metric("RTT supported", tm.isRttSupported()));
        gm.add(new Metric ("SMS capable",tm.isSmsCapable()));
        //gm.add(new Metric ("TTY mode support",tm.isTtyModeSupported()));
        gm.add(new Metric ("voice capable",tm.isVoiceCapable()));
        gm.add(new Metric ("world phone",tm.isWorldPhone()));

        //// BELOW METRICS ARE RESTRICTED BY READ_PHONE_STATE PERMISSION ////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            gm.add(new Metric ("data enabled",tm.isDataEnabled()));
            sm.add(new Metric("multi-SIM config-edit req. reboot", tm.doesSwitchMultiSimConfigTriggerReboot()));
            gm.add(new Metric("subscription call state", tm.getCallStateForSubscription()));
            gm.add(new Metric ("data network type", getNetworkType(tm.getDataNetworkType())));
            gm.add(new Metric ("device software version",tm.getDeviceSoftwareVersion()));
            gm.add(new Metric("emergency number list", String.valueOf(tm.getEmergencyNumberList())));
            gm.add(new Metric("equivalent home PLMNs", String.valueOf(tm.getEquivalentHomePlmns())));
            gm.add(new Metric ("forbidden PLMNs", Arrays.toString(tm.getForbiddenPlmns())));
            gm.add(new Metric ("group ID level 1",tm.getGroupIdLevel1()));
            gm.add(new Metric("preferred data subscription", tm.getPreferredOpportunisticDataSubscription()));
            gm.add(new Metric ("voicemail number",tm.getVoiceMailNumber()));
            gm.add(new Metric ("voice network type", getNetworkType(tm.getVoiceNetworkType())));
            gm.add(new Metric("data connection allowed", tm.isDataConnectionAllowed()));
            gm.add(new Metric("data roaming enabled", tm.isDataRoamingEnabled()));

            sm.add(new Metric("slot 1: modem enabled", tm.isModemEnabledForSlot(0)));
            sm.add(new Metric("slot 2: modem enabled", tm.isModemEnabledForSlot(1)));
            sm.add(new Metric("multi-SIM supported", getMultiSimCapability(tm.isMultiSimSupported())));
        } //else {
        //    String msg = "--READ_PHONE_STATE permission required--";
        //    gm.add(new Metric ("Data enabled",msg));
        //    sm.add(new Metric("multi-SIM config-edit req. reboot", msg));
        //    gm.add(new Metric("Subscription Call state", msg));
        //    gm.add(new Metric ("Data Network Type", msg));
        //    gm.add(new Metric ("Device Software Version",msg));
        //    gm.add(new Metric("Emergency Number List", msg));
        //    gm.add(new Metric("Equivalent Home PLMNs", msg));
        //    gm.add(new Metric ("Forbidden PLMNs", msg));
        //    gm.add(new Metric ("Group ID Level 1",msg));
        //    gm.add(new Metric("Preferred Data Subscription", msg));
        //    gm.add(new Metric ("Voicemail number",msg));
        //    gm.add(new Metric ("Voice Network type", msg));
        //    gm.add(new Metric("Data Connection allowed", msg));
        //    gm.add(new Metric("Data Roaming enabled", msg));
        //    sm.add(new Metric("Slot 1: Modem enabled", msg));
        //    sm.add(new Metric("Slot 2: Modem enabled", msg));
        //    sm.add(new Metric("Multi-SIM supported", msg));

        //}

        //tm.canChangeDtmfToneLength();
        //tm.doesSwitchMultiSimConfigTriggerReboot();
        //tm.getActiveModemCount();
        //tm.getAllCellInfo();
        //tm.getAllowedNetworkTypesForReason();
        //tm.getCallComposerStatus();
        //tm.getCallStateForSubscription();
        //tm.getCardIdForDefaultEuicc();
        //tm.getCarrierIdFromSimMccMnc();
        //tm.getCarrierRestrictionStatus();
        //tm.getDataActivity();
        //tm.getDataNetworkType();
        //tm.getDataState();
        //tm.getDeviceSoftwareVersion();
        //tm.getSim

        //tm.


        //tm.getNetworkSelectionMode();
        //tm.getImei(0);
        //tm.getImei(1);
        //tm.getMeid(0);
        //tm.getMeid(1);
        //tm.isManualNetworkSelectionAllowed();
        //tm.getSubscriberId();
        //tm.getSimSerialNumber();
        //tm.



        displayMetrics(findViewById(generalId), gm);
        displayMetrics(findViewById(simId), sm);
    }
    private String getSimState(int code) {
        switch (code) {
            case 0: return "unknown";
            case 1: return "absent";
            case 2: return "PIN_REQUIRED";
            case 3: return "PUK_REQUIRED";
            case 4: return "Network Locked";
            case 5: return "READY";
            case 6: return "NOT_READY";
            case 7: return "PERMANENTLY_DISABLED";
            case 8: return "CARD_IO_ERROR";
            case 9: return "CARD_RESTRICTED";
            default: return "--error: unknown sim-state code--";
        }
    }
    private String getMultiSimCapability(int code) {
        switch (code) {
            case 0: return "ALLOWED";
            case 1: return "NOT_ALLOWED_BY_HARDWARE";
            case 2: return "NOT_ALLOWED_BY_CARRIER";
            default: return "--error: unknown multi-sim capability code--";
        }
    }
    private String getNetworkType(int code) {
        switch (code) {
            case 0: return "UNKNOWN";
            case 1: return "GPRS";
            case 2: return "EDGE";
            case 3: return "UMTS";
            case 4: return "CDMA";
            case 5: return "EVDO_0";
            case 6: return "EVDO_A";
            case 7: return "1xRTT";
            case 8: return "HSDPA";
            case 9: return "HSUPA";
            case 10: return "HSPA";
            case 11: return "IDEN";
            case 12: return "EVDO_B";
            case 13: return "LTE";
            case 14: return "EHRPD";
            case 15: return "HSPAP";
            case 16: return "GSM";
            case 17: return "TD_SCDMA";
            case 18: return "IWLAN";
            //case 19: return "unkn";
            case 20: return "NR";
            default: return "--error: unknown network-type-code--";
        }
    }
    private String getPhoneType(int code) {
        switch (code) {
            case 0: return "NONE";
            case 1: return "GSM";
            case 2: return "CDMA";
            case 3: return "SIP";
            default: return "--error: unknown phone-type-code--";
        }
    }

    private String DataStatus(int code) {
        switch (code) {
            case TelephonyManager.DATA_CONNECTED:
                return "DATA_CONNECTED";
            case TelephonyManager.DATA_CONNECTING:
                return "DATA_CONNECTING";
            case TelephonyManager.DATA_DISCONNECTED:
                return "DATA_DISCONNECTED";
            case TelephonyManager.DATA_DISCONNECTING:
                return "DATA_DISCONNECTING";
            case TelephonyManager.DATA_SUSPENDED:
                return "DATA_SUSPENDED";
            case TelephonyManager.DATA_HANDOVER_IN_PROGRESS:
                return "DATA_HANDOVER_IN_PROGRESS";
            default:
                return "--error: unknown phone-type-code--";
        }
    }

}
