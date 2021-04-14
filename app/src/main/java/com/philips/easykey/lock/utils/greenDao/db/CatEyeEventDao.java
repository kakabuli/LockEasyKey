package com.philips.easykey.lock.utils.greenDao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.philips.easykey.lock.utils.greenDao.bean.CatEyeEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CAT_EYE_EVENT".
*/
public class CatEyeEventDao extends AbstractDao<CatEyeEvent, Long> {

    public static final String TABLENAME = "CAT_EYE_EVENT";

    /**
     * Properties of entity CatEyeEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeviceType = new Property(1, int.class, "deviceType", false, "DEVICE_TYPE");
        public final static Property DeviceId = new Property(2, String.class, "deviceId", false, "DEVICE_ID");
        public final static Property GatewayId = new Property(3, String.class, "gatewayId", false, "GATEWAY_ID");
        public final static Property EventType = new Property(4, int.class, "eventType", false, "EVENT_TYPE");
        public final static Property EventTime = new Property(5, long.class, "eventTime", false, "EVENT_TIME");
    }


    public CatEyeEventDao(DaoConfig config) {
        super(config);
    }
    
    public CatEyeEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CAT_EYE_EVENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DEVICE_TYPE\" INTEGER NOT NULL ," + // 1: deviceType
                "\"DEVICE_ID\" TEXT," + // 2: deviceId
                "\"GATEWAY_ID\" TEXT," + // 3: gatewayId
                "\"EVENT_TYPE\" INTEGER NOT NULL ," + // 4: eventType
                "\"EVENT_TIME\" INTEGER NOT NULL );"); // 5: eventTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CAT_EYE_EVENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CatEyeEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDeviceType());
 
        String deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindString(3, deviceId);
        }
 
        String gatewayId = entity.getGatewayId();
        if (gatewayId != null) {
            stmt.bindString(4, gatewayId);
        }
        stmt.bindLong(5, entity.getEventType());
        stmt.bindLong(6, entity.getEventTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CatEyeEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDeviceType());
 
        String deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindString(3, deviceId);
        }
 
        String gatewayId = entity.getGatewayId();
        if (gatewayId != null) {
            stmt.bindString(4, gatewayId);
        }
        stmt.bindLong(5, entity.getEventType());
        stmt.bindLong(6, entity.getEventTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CatEyeEvent readEntity(Cursor cursor, int offset) {
        CatEyeEvent entity = new CatEyeEvent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // deviceType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // deviceId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // gatewayId
            cursor.getInt(offset + 4), // eventType
            cursor.getLong(offset + 5) // eventTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CatEyeEvent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceType(cursor.getInt(offset + 1));
        entity.setDeviceId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGatewayId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEventType(cursor.getInt(offset + 4));
        entity.setEventTime(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CatEyeEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CatEyeEvent entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CatEyeEvent entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
