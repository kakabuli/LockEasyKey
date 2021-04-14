package com.philips.easykey.lock.utils.greenDao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.philips.easykey.lock.utils.greenDao.bean.BleLockServiceInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BLE_LOCK_SERVICE_INFO".
*/
public class BleLockServiceInfoDao extends AbstractDao<BleLockServiceInfo, Long> {

    public static final String TABLENAME = "BLE_LOCK_SERVICE_INFO";

    /**
     * Properties of entity BleLockServiceInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property LockName = new Property(1, String.class, "lockName", false, "LOCK_NAME");
        public final static Property LockNickName = new Property(2, String.class, "lockNickName", false, "LOCK_NICK_NAME");
        public final static Property MacLock = new Property(3, String.class, "macLock", false, "MAC_LOCK");
        public final static Property Open_purview = new Property(4, String.class, "open_purview", false, "OPEN_PURVIEW");
        public final static Property Is_admin = new Property(5, String.class, "is_admin", false, "IS_ADMIN");
        public final static Property Center_latitude = new Property(6, String.class, "center_latitude", false, "CENTER_LATITUDE");
        public final static Property Center_longitude = new Property(7, String.class, "center_longitude", false, "CENTER_LONGITUDE");
        public final static Property Circle_radius = new Property(8, String.class, "circle_radius", false, "CIRCLE_RADIUS");
        public final static Property Auto_lock = new Property(9, String.class, "auto_lock", false, "AUTO_LOCK");
        public final static Property Password1 = new Property(10, String.class, "password1", false, "PASSWORD1");
        public final static Property Password2 = new Property(11, String.class, "password2", false, "PASSWORD2");
        public final static Property Model = new Property(12, String.class, "model", false, "MODEL");
        public final static Property Uid = new Property(13, String.class, "uid", false, "UID");
        public final static Property SoftwareVersion = new Property(14, String.class, "softwareVersion", false, "SOFTWARE_VERSION");
        public final static Property DeviceSN = new Property(15, String.class, "deviceSN", false, "DEVICE_SN");
        public final static Property BleVersion = new Property(16, String.class, "bleVersion", false, "BLE_VERSION");
        public final static Property CreateTime = new Property(17, long.class, "createTime", false, "CREATE_TIME");
        public final static Property FunctionSet = new Property(18, String.class, "functionSet", false, "FUNCTION_SET");
    }


    public BleLockServiceInfoDao(DaoConfig config) {
        super(config);
    }
    
    public BleLockServiceInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BLE_LOCK_SERVICE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LOCK_NAME\" TEXT," + // 1: lockName
                "\"LOCK_NICK_NAME\" TEXT," + // 2: lockNickName
                "\"MAC_LOCK\" TEXT," + // 3: macLock
                "\"OPEN_PURVIEW\" TEXT," + // 4: open_purview
                "\"IS_ADMIN\" TEXT," + // 5: is_admin
                "\"CENTER_LATITUDE\" TEXT," + // 6: center_latitude
                "\"CENTER_LONGITUDE\" TEXT," + // 7: center_longitude
                "\"CIRCLE_RADIUS\" TEXT," + // 8: circle_radius
                "\"AUTO_LOCK\" TEXT," + // 9: auto_lock
                "\"PASSWORD1\" TEXT," + // 10: password1
                "\"PASSWORD2\" TEXT," + // 11: password2
                "\"MODEL\" TEXT," + // 12: model
                "\"UID\" TEXT," + // 13: uid
                "\"SOFTWARE_VERSION\" TEXT," + // 14: softwareVersion
                "\"DEVICE_SN\" TEXT," + // 15: deviceSN
                "\"BLE_VERSION\" TEXT," + // 16: bleVersion
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 17: createTime
                "\"FUNCTION_SET\" TEXT);"); // 18: functionSet
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BLE_LOCK_SERVICE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BleLockServiceInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String lockName = entity.getLockName();
        if (lockName != null) {
            stmt.bindString(2, lockName);
        }
 
        String lockNickName = entity.getLockNickName();
        if (lockNickName != null) {
            stmt.bindString(3, lockNickName);
        }
 
        String macLock = entity.getMacLock();
        if (macLock != null) {
            stmt.bindString(4, macLock);
        }
 
        String open_purview = entity.getOpen_purview();
        if (open_purview != null) {
            stmt.bindString(5, open_purview);
        }
 
        String is_admin = entity.getIs_admin();
        if (is_admin != null) {
            stmt.bindString(6, is_admin);
        }
 
        String center_latitude = entity.getCenter_latitude();
        if (center_latitude != null) {
            stmt.bindString(7, center_latitude);
        }
 
        String center_longitude = entity.getCenter_longitude();
        if (center_longitude != null) {
            stmt.bindString(8, center_longitude);
        }
 
        String circle_radius = entity.getCircle_radius();
        if (circle_radius != null) {
            stmt.bindString(9, circle_radius);
        }
 
        String auto_lock = entity.getAuto_lock();
        if (auto_lock != null) {
            stmt.bindString(10, auto_lock);
        }
 
        String password1 = entity.getPassword1();
        if (password1 != null) {
            stmt.bindString(11, password1);
        }
 
        String password2 = entity.getPassword2();
        if (password2 != null) {
            stmt.bindString(12, password2);
        }
 
        String model = entity.getModel();
        if (model != null) {
            stmt.bindString(13, model);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(14, uid);
        }
 
        String softwareVersion = entity.getSoftwareVersion();
        if (softwareVersion != null) {
            stmt.bindString(15, softwareVersion);
        }
 
        String deviceSN = entity.getDeviceSN();
        if (deviceSN != null) {
            stmt.bindString(16, deviceSN);
        }
 
        String bleVersion = entity.getBleVersion();
        if (bleVersion != null) {
            stmt.bindString(17, bleVersion);
        }
        stmt.bindLong(18, entity.getCreateTime());
 
        String functionSet = entity.getFunctionSet();
        if (functionSet != null) {
            stmt.bindString(19, functionSet);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BleLockServiceInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String lockName = entity.getLockName();
        if (lockName != null) {
            stmt.bindString(2, lockName);
        }
 
        String lockNickName = entity.getLockNickName();
        if (lockNickName != null) {
            stmt.bindString(3, lockNickName);
        }
 
        String macLock = entity.getMacLock();
        if (macLock != null) {
            stmt.bindString(4, macLock);
        }
 
        String open_purview = entity.getOpen_purview();
        if (open_purview != null) {
            stmt.bindString(5, open_purview);
        }
 
        String is_admin = entity.getIs_admin();
        if (is_admin != null) {
            stmt.bindString(6, is_admin);
        }
 
        String center_latitude = entity.getCenter_latitude();
        if (center_latitude != null) {
            stmt.bindString(7, center_latitude);
        }
 
        String center_longitude = entity.getCenter_longitude();
        if (center_longitude != null) {
            stmt.bindString(8, center_longitude);
        }
 
        String circle_radius = entity.getCircle_radius();
        if (circle_radius != null) {
            stmt.bindString(9, circle_radius);
        }
 
        String auto_lock = entity.getAuto_lock();
        if (auto_lock != null) {
            stmt.bindString(10, auto_lock);
        }
 
        String password1 = entity.getPassword1();
        if (password1 != null) {
            stmt.bindString(11, password1);
        }
 
        String password2 = entity.getPassword2();
        if (password2 != null) {
            stmt.bindString(12, password2);
        }
 
        String model = entity.getModel();
        if (model != null) {
            stmt.bindString(13, model);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(14, uid);
        }
 
        String softwareVersion = entity.getSoftwareVersion();
        if (softwareVersion != null) {
            stmt.bindString(15, softwareVersion);
        }
 
        String deviceSN = entity.getDeviceSN();
        if (deviceSN != null) {
            stmt.bindString(16, deviceSN);
        }
 
        String bleVersion = entity.getBleVersion();
        if (bleVersion != null) {
            stmt.bindString(17, bleVersion);
        }
        stmt.bindLong(18, entity.getCreateTime());
 
        String functionSet = entity.getFunctionSet();
        if (functionSet != null) {
            stmt.bindString(19, functionSet);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BleLockServiceInfo readEntity(Cursor cursor, int offset) {
        BleLockServiceInfo entity = new BleLockServiceInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // lockName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // lockNickName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // macLock
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // open_purview
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // is_admin
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // center_latitude
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // center_longitude
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // circle_radius
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // auto_lock
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // password1
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // password2
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // model
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // uid
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // softwareVersion
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // deviceSN
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // bleVersion
            cursor.getLong(offset + 17), // createTime
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18) // functionSet
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BleLockServiceInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLockName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLockNickName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMacLock(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setOpen_purview(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIs_admin(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCenter_latitude(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCenter_longitude(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCircle_radius(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAuto_lock(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPassword1(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPassword2(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setModel(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUid(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSoftwareVersion(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setDeviceSN(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setBleVersion(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setCreateTime(cursor.getLong(offset + 17));
        entity.setFunctionSet(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BleLockServiceInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BleLockServiceInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BleLockServiceInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
