package org.shirakawatyu.dc.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import org.shirakawatyu.dc.entity.Status;
import org.shirakawatyu.dc.util.CommandUtil;
import org.shirakawatyu.dc.util.DisplayUtil;
import static android.service.quicksettings.Tile.STATE_ACTIVE;
import static android.service.quicksettings.Tile.STATE_INACTIVE;

public class AntiFlickerModeTileService extends TileService {
    private final Status status = new Status();

    @Override
    public void onTileAdded() {
        this.onStartListening();
    }

    @Override
    public void onStartListening() {
        boolean dcStatus = getSharedPreferences("dc", MODE_PRIVATE).getBoolean("dcStatus", false);
        Tile qsTile = getQsTile();
        qsTile.setState(dcStatus ? STATE_ACTIVE : STATE_INACTIVE);
        qsTile.updateTile();
    }

    @Override
    public void onClick() {
        Tile qsTile = getQsTile();
        boolean dcStatus = getSharedPreferences("dc", MODE_PRIVATE).getBoolean("dcStatus", false);
        DisplayUtil.simpleSetAntiFlickerMode(getApplicationContext(), status, !dcStatus);
        qsTile.setState(!dcStatus? STATE_ACTIVE : STATE_INACTIVE);
        qsTile.updateTile();
    }

}
