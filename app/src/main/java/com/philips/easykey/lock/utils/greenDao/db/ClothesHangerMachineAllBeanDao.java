package com.philips.easykey.lock.utils.greenDao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineLightingBean;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineMotorBean;
import com.philips.easykey.lock.utils.greenDao.convert.ClothesHangerMachineLightConvert;
import com.philips.easykey.lock.utils.greenDao.convert.ClothesHangerMachineMotorConvert;

import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CLOTHES_HANGER_MACHINE_ALL_BEAN".
*/
public class ClothesHangerMachineAllBeanDao extends AbstractDao<ClothesHangerMachineAllBean, Long> {

    public static final String TABLENAME = "CLOTHES_HANGER_MACHINE_ALL_BEAN";

    /**
     * Properties of entity ClothesHangerMachineAllBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeviceID = new Property(1, String.class, "deviceID", false, "DEVICE_ID");
        public final static Property WifiSN = new Property(2, String.class, "wifiSN", false, "WIFI_SN");
        public final static Property AdminName = new Property(3, String.class, "adminName", false, "ADMIN_NAME");
        public final static Property AdminUid = new Property(4, String.class, "adminUid", false, "ADMIN_UID");
        public final static Property CreateTime = new Property(5, long.class, "createTime", false, "CREATE_TIME");
        public final static Property HangerNickName = new Property(6, String.class, "hangerNickName", false, "HANGER_NICK_NAME");
        public final static Property IsAdmin = new Property(7, int.class, "isAdmin", false, "IS_ADMIN");
        public final static Property Uid = new Property(8, String.class, "uid", false, "UID");
        public final static Property Uname = new Property(9, String.class, "uname", false, "UNAME");
        public final static Property UpdateTime = new Property(10, long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property HangerSN = new Property(11, String.class, "hangerSN", false, "HANGER_SN");
        public final static Property HangerVersion = new Property(12, String.class, "hangerVersion", false, "HANGER_VERSION");
        public final static Property ModuleSN = new Property(13, String.class, "moduleSN", false, "MODULE_SN");
        public final static Property ModuleVersion = new Property(14, String.class, "moduleVersion", false, "MODULE_VERSION");
        public final static Property Loudspeaker = new Property(15, int.class, "loudspeaker", false, "LOUDSPEAKER");
        public final static Property ChildLock = new Property(16, int.class, "childLock", false, "CHILD_LOCK");
        public final static Property Overload = new Property(17, int.class, "overload", false, "OVERLOAD");
        public final static Property Status = new Property(18, int.class, "status", false, "STATUS");
        public final static Property Motor = new Property(19, String.class, "motor", false, "MOTOR");
        public final static Property UV = new Property(20, String.class, "UV", false, "UV");
        public final static Property AirDry = new Property(21, String.class, "airDry", false, "AIR_DRY");
        public final static Property Baking = new Property(22, String.class, "baking", false, "BAKING");
        public final static Property Light = new Property(23, String.class, "light", false, "LIGHT");
    }

    private final ClothesHangerMachineMotorConvert motorConverter = new ClothesHangerMachineMotorConvert();
    private final ClothesHangerMachineLightConvert UVConverter = new ClothesHangerMachineLightConvert();
    private final ClothesHangerMachineLightConvert airDryConverter = new ClothesHangerMachineLightConvert();
    private final ClothesHangerMachineLightConvert bakingConverter = new ClothesHangerMachineLightConvert();
    private final ClothesHangerMachineLightConvert lightConverter = new ClothesHangerMachineLightConvert();

    public ClothesHangerMachineAllBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ClothesHangerMachineAllBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CLOTHES_HANGER_MACHINE_ALL_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DEVICE_ID\" TEXT," + // 1: deviceID
                "\"WIFI_SN\" TEXT," + // 2: wifiSN
                "\"ADMIN_NAME\" TEXT," + // 3: adminName
                "\"ADMIN_UID\" TEXT," + // 4: adminUid
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 5: createTime
                "\"HANGER_NICK_NAME\" TEXT," + // 6: hangerNickName
                "\"IS_ADMIN\" INTEGER NOT NULL ," + // 7: isAdmin
                "\"UID\" TEXT," + // 8: uid
                "\"UNAME\" TEXT," + // 9: uname
                "\"UPDATE_TIME\" INTEGER NOT NULL ," + // 10: updateTime
                "\"HANGER_SN\" TEXT," + // 11: hangerSN
                "\"HANGER_VERSION\" TEXT," + // 12: hangerVersion
                "\"MODULE_SN\" TEXT," + // 13: moduleSN
                "\"MODULE_VERSION\" TEXT," + // 14: moduleVersion
                "\"LOUDSPEAKER\" INTEGER NOT NULL ," + // 15: loudspeaker
                "\"CHILD_LOCK\" INTEGER NOT NULL ," + // 16: childLock
                "\"OVERLOAD\" INTEGER NOT NULL ," + // 17: overload
                "\"STATUS\" INTEGER NOT NULL ," + // 18: status
                "\"MOTOR\" TEXT," + // 19: motor
                "\"UV\" TEXT," + // 20: UV
                "\"AIR_DRY\" TEXT," + // 21: airDry
                "\"BAKING\" TEXT," + // 22: baking
                "\"LIGHT\" TEXT);"); // 23: light
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CLOTHES_HANGER_MACHINE_ALL_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ClothesHangerMachineAllBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceID = entity.getDeviceID();
        if (deviceID != null) {
            stmt.bindString(2, deviceID);
        }
 
        String wifiSN = entity.getWifiSN();
        if (wifiSN != null) {
            stmt.bindString(3, wifiSN);
        }
 
        String adminName = entity.getAdminName();
        if (adminName != null) {
            stmt.bindString(4, adminName);
        }
 
        String adminUid = entity.getAdminUid();
        if (adminUid != null) {
            stmt.bindString(5, adminUid);
        }
        stmt.bindLong(6, entity.getCreateTime());
 
        String hangerNickName = entity.getHangerNickName();
        if (hangerNickName != null) {
            stmt.bindString(7, hangerNickName);
        }
        stmt.bindLong(8, entity.getIsAdmin());
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(9, uid);
        }
 
        String uname = entity.getUname();
        if (uname != null) {
            stmt.bindString(10, uname);
        }
        stmt.bindLong(11, entity.getUpdateTime());
 
        String hangerSN = entity.getHangerSN();
        if (hangerSN != null) {
            stmt.bindString(12, hangerSN);
        }
 
        String hangerVersion = entity.getHangerVersion();
        if (hangerVersion != null) {
            stmt.bindString(13, hangerVersion);
        }
 
        String moduleSN = entity.getModuleSN();
        if (moduleSN != null) {
            stmt.bindString(14, moduleSN);
        }
 
        String moduleVersion = entity.getModuleVersion();
        if (moduleVersion != null) {
            stmt.bindString(15, moduleVersion);
        }
        stmt.bindLong(16, entity.getLoudspeaker());
        stmt.bindLong(17, entity.getChildLock());
        stmt.bindLong(18, entity.getOverload());
        stmt.bindLong(19, entity.getStatus());
 
        ClothesHangerMachineMotorBean motor = entity.getMotor();
        if (motor != null) {
            stmt.bindString(20, motorConverter.convertToDatabaseValue(motor));
        }
 
        ClothesHangerMachineLightingBean UV = entity.getUV();
        if (UV != null) {
            stmt.bindString(21, UVConverter.convertToDatabaseValue(UV));
        }
 
        ClothesHangerMachineLightingBean airDry = entity.getAirDry();
        if (airDry != null) {
            stmt.bindString(22, airDryConverter.convertToDatabaseValue(airDry));
        }
 
        ClothesHangerMachineLightingBean baking = entity.getBaking();
        if (baking != null) {
            stmt.bindString(23, bakingConverter.convertToDatabaseValue(baking));
        }
 
        ClothesHangerMachineLightingBean light = entity.getLight();
        if (light != null) {
            stmt.bindString(24, lightConverter.convertToDatabaseValue(light));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ClothesHangerMachineAllBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceID = entity.getDeviceID();
        if (deviceID != null) {
            stmt.bindString(2, deviceID);
        }
 
        String wifiSN = entity.getWifiSN();
        if (wifiSN != null) {
            stmt.bindString(3, wifiSN);
        }
 
        String adminName = entity.getAdminName();
        if (adminName != null) {
            stmt.bindString(4, adminName);
        }
 
        String adminUid = entity.getAdminUid();
        if (adminUid != null) {
            stmt.bindString(5, adminUid);
        }
        stmt.bindLong(6, entity.getCreateTime());
 
        String hangerNickName = entity.getHangerNickName();
        if (hangerNickName != null) {
            stmt.bindString(7, hangerNickName);
        }
        stmt.bindLong(8, entity.getIsAdmin());
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(9, uid);
        }
 
        String uname = entity.getUname();
        if (uname != null) {
            stmt.bindString(10, uname);
        }
        stmt.bindLong(11, entity.getUpdateTime());
 
        String hangerSN = entity.getHangerSN();
        if (hangerSN != null) {
            stmt.bindString(12, hangerSN);
        }
 
        String hangerVersion = entity.getHangerVersion();
        if (hangerVersion != null) {
            stmt.bindString(13, hangerVersion);
        }
 
        String moduleSN = entity.getModuleSN();
        if (moduleSN != null) {
            stmt.bindString(14, moduleSN);
        }
 
        String moduleVersion = entity.getModuleVersion();
        if (moduleVersion != null) {
            stmt.bindString(15, moduleVersion);
        }
        stmt.bindLong(16, entity.getLoudspeaker());
        stmt.bindLong(17, entity.getChildLock());
        stmt.bindLong(18, entity.getOverload());
        stmt.bindLong(19, entity.getStatus());
 
        ClothesHangerMachineMotorBean motor = entity.getMotor();
        if (motor != null) {
            stmt.bindString(20, motorConverter.convertToDatabaseValue(motor));
        }
 
        ClothesHangerMachineLightingBean UV = entity.getUV();
        if (UV != null) {
            stmt.bindString(21, UVConverter.convertToDatabaseValue(UV));
        }
 
        ClothesHangerMachineLightingBean airDry = entity.getAirDry();
        if (airDry != null) {
            stmt.bindString(22, airDryConverter.convertToDatabaseValue(airDry));
        }
 
        ClothesHangerMachineLightingBean baking = entity.getBaking();
        if (baking != null) {
            stmt.bindString(23, bakingConverter.convertToDatabaseValue(baking));
        }
 
        ClothesHangerMachineLightingBean light = entity.getLight();
        if (light != null) {
            stmt.bindString(24, lightConverter.convertToDatabaseValue(light));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ClothesHangerMachineAllBean readEntity(Cursor cursor, int offset) {
        ClothesHangerMachineAllBean entity = new ClothesHangerMachineAllBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // deviceID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // wifiSN
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // adminName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // adminUid
            cursor.getLong(offset + 5), // createTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // hangerNickName
            cursor.getInt(offset + 7), // isAdmin
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // uid
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // uname
            cursor.getLong(offset + 10), // updateTime
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // hangerSN
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // hangerVersion
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // moduleSN
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // moduleVersion
            cursor.getInt(offset + 15), // loudspeaker
            cursor.getInt(offset + 16), // childLock
            cursor.getInt(offset + 17), // overload
            cursor.getInt(offset + 18), // status
            cursor.isNull(offset + 19) ? null : motorConverter.convertToEntityProperty(cursor.getString(offset + 19)), // motor
            cursor.isNull(offset + 20) ? null : UVConverter.convertToEntityProperty(cursor.getString(offset + 20)), // UV
            cursor.isNull(offset + 21) ? null : airDryConverter.convertToEntityProperty(cursor.getString(offset + 21)), // airDry
            cursor.isNull(offset + 22) ? null : bakingConverter.convertToEntityProperty(cursor.getString(offset + 22)), // baking
            cursor.isNull(offset + 23) ? null : lightConverter.convertToEntityProperty(cursor.getString(offset + 23)) // light
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ClothesHangerMachineAllBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setWifiSN(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAdminName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAdminUid(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreateTime(cursor.getLong(offset + 5));
        entity.setHangerNickName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsAdmin(cursor.getInt(offset + 7));
        entity.setUid(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUname(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUpdateTime(cursor.getLong(offset + 10));
        entity.setHangerSN(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setHangerVersion(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setModuleSN(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setModuleVersion(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setLoudspeaker(cursor.getInt(offset + 15));
        entity.setChildLock(cursor.getInt(offset + 16));
        entity.setOverload(cursor.getInt(offset + 17));
        entity.setStatus(cursor.getInt(offset + 18));
        entity.setMotor(cursor.isNull(offset + 19) ? null : motorConverter.convertToEntityProperty(cursor.getString(offset + 19)));
        entity.setUV(cursor.isNull(offset + 20) ? null : UVConverter.convertToEntityProperty(cursor.getString(offset + 20)));
        entity.setAirDry(cursor.isNull(offset + 21) ? null : airDryConverter.convertToEntityProperty(cursor.getString(offset + 21)));
        entity.setBaking(cursor.isNull(offset + 22) ? null : bakingConverter.convertToEntityProperty(cursor.getString(offset + 22)));
        entity.setLight(cursor.isNull(offset + 23) ? null : lightConverter.convertToEntityProperty(cursor.getString(offset + 23)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ClothesHangerMachineAllBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ClothesHangerMachineAllBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ClothesHangerMachineAllBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
