package com.philips.easykey.lock.bean;

import com.philips.easykey.lock.publiclibrary.ble.bean.OperationLockRecord;

public class OperationSection extends SectionEntity<OperationLockRecord> {
    public boolean isHeader() {
        return isHeader;
    }

    private boolean isHeader;

    public OperationSection(boolean isHeader, String header) {
        super(isHeader, header);
        this.isHeader = isHeader;
    }

    public OperationSection(OperationLockRecord t) {
        super(t);
    }
}
