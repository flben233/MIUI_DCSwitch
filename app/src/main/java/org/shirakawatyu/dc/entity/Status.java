package org.shirakawatyu.dc.entity;

public class Status {
    private boolean lastSELinuxStatus;

    public Status() {
        this.lastSELinuxStatus = false;
    }

    public boolean getLastSELinuxStatus() {
        return lastSELinuxStatus;
    }

    public void setLastSELinuxStatus(boolean lastSELinuxStatus) {
        this.lastSELinuxStatus = lastSELinuxStatus;
    }
}
