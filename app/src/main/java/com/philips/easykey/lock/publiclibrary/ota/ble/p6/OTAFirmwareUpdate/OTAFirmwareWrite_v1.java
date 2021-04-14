/*
 * Copyright Cypress Semiconductor Corporation, 2014-2018 All rights reserved.
 *
 * This software, associated documentation and materials ("Software") is
 * owned by Cypress Semiconductor Corporation ("Cypress") and is
 * protected by and subject to worldwide patent protection (UnitedStates and foreign),
 * United States copyright laws and international
 * treaty provisions. Therefore, unless otherwise specified in a separate
 * license agreement between you and Cypress, this Software
 * must be treated like any other copyrighted material. Reproduction,
 * modification, translation, compilation, or representation of this
 * Software in any other form (e.g., paper, magnetic, optical, silicon)
 * is prohibited without Cypress's express written permission.
 *
 * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * NONINFRINGEMENT, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE. Cypress reserves the right to make changes
 * to the Software without notice. Cypress does not assume any liability
 * arising out of the application or use of Software or any product or
 * circuit described in the Software. Cypress does not authorize its
 * products for use as critical components in any products where a
 * malfunction or failure may reasonably be expected to result in
 * significant injury or death ("High Risk Product"). By including
 * Cypress's product in a High Risk Product, the manufacturer of such
 * system or application assumes all risk of such use and in doing so
 * indemnifies Cypress against all liability.
 *
 * Use of this Software may be limited by and subject to the applicable
 * Cypress software license agreement.
 *
 *
 */

package com.philips.easykey.lock.publiclibrary.ota.ble.p6.OTAFirmwareUpdate;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.philips.easykey.lock.publiclibrary.ota.ble.p6.BluetoothLeService;
import com.philips.easykey.lock.publiclibrary.ota.ble.p6.CommonUtils.CheckSumUtils;
import com.philips.easykey.lock.publiclibrary.ota.ble.p6.CommonUtils.ConvertUtils;
import com.philips.easykey.lock.utils.Rsa;

/**
 * Separate class for handling the write operation during OTA firmware upgrade
 */
public class OTAFirmwareWrite_v1 {
    private static final int NON_CHECKSUMMABLE_SIZE = 3;//exclude checksum(2) + EOP(1)
    private BluetoothGattCharacteristic mOtaCharacteristic;
    private String TAG = "OTA升级";


    public OTAFirmwareWrite_v1(BluetoothGattCharacteristic otaCharacteristic) {
        this.mOtaCharacteristic = otaCharacteristic;
    }

    public void OTAEnterBootLoaderCmd(byte checkSumType, byte[] productId) {
        int idx = 0;
        final int dataLength = 6;//productId(4) + zero bytes(2)//TODO
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.ENTER_BOOTLOADER;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        //Data
        for (int i = 0; i < 4; i++) {
            commandBytes[idx++] = productId[i];
        }
        commandBytes[idx++] = 0;
        commandBytes[idx++] = 0;
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTAEnterBootLoaderCmd  长度 "+ commandBytes.length+"  " + Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }

    public void OTASetAppMetadataCmd(byte checkSumType, byte appId, byte[] appStart, byte[] appSize) {
        int idx = 0;
        final int dataLength = 9;//appId(1) + appStart(4) + appSize(4)
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.SET_APP_METADATA;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        //Data
        commandBytes[idx++] = appId;
        for (int i = 0; i < 4; i++) {
            commandBytes[idx++] = appStart[i];
        }
        for (int i = 0; i < 4; i++) {
            commandBytes[idx++] = appSize[i];
        }
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTASetAppMetadataCmd  长度 "+ commandBytes.length+"  " + Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }

    public void OTASetEivCmd(byte checkSumType, byte[] data) {
        int idx = 0;
        final int dataLength = data.length;
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.SET_EIV;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        //Data
        for (int i = 0; i < data.length; i++) {
            commandBytes[idx++] = data[i];
        }
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTASetEivCmd   长度 "+ commandBytes.length+"  "+ Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }


    public void OTASendDataCmd(byte checkSumType, byte[] data) {
        int idx = 0;
        final int dataLength = data.length;
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.SEND_DATA;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        //Data
        for (int i = 0; i < data.length; i++) {
            commandBytes[idx++] = data[i];
        }
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTASendDataCmd  长度 "+ commandBytes.length+"  " + Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }

    public void OTAProgramDataCmd(byte checkSumType, byte[] address, byte[] crc32, byte[] data) {
        int idx = 0;
        final int dataLength = 8 + data.length;//address(4) + crc32(4) + data.length
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PROGRAM_DATA;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        //Data
        for (int i = 0; i < 4; i++) {
            commandBytes[idx++] = address[i];
        }
        for (int i = 0; i < 4; i++) {
            commandBytes[idx++] = crc32[i];
        }
        for (int i = 0; i < data.length; i++) {
            commandBytes[idx++] = data[i];
        }
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTAProgramDataCmd   长度 "+ commandBytes.length+"  " + Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }

    public void OTAVerifyAppCmd(byte checkSumType, byte appId) {
        int idx = 0;
        final int dataLength = 1;//appId(1)
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.VERIFY_APP;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        commandBytes[idx++] = appId;//Data
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTAVerifyAppCmd   长度 "+ commandBytes.length+"  " + Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes);
    }

    public void OTAExitBootloaderCmd(byte checkSumType) {
        int idx = 0;
        final int dataLength = 0;
        final int commandSize = BootLoaderCommands_v1.BASE_CMD_SIZE + dataLength;
        final int checksumableDataSize = commandSize - NON_CHECKSUMMABLE_SIZE;

        byte[] commandBytes = new byte[commandSize];
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_START;//Start of packet
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.EXIT_BOOTLOADER;//Command
        commandBytes[idx++] = (byte) dataLength;//Data length
        commandBytes[idx++] = (byte) (dataLength >> 8);//Data length
        int checkSum = CheckSumUtils.calculateCheckSum2(ConvertUtils.byteToIntUnsigned(checkSumType), commandBytes, checksumableDataSize);
        commandBytes[idx++] = (byte) checkSum;//Checksum
        commandBytes[idx++] = (byte) (checkSum >> 8);//Checksum
        commandBytes[idx++] = (byte) BootLoaderCommands_v1.PACKET_END;//End of packet

        Log.e(TAG, "初始化指令  OTAExitBootloaderCmd   长度 "+ commandBytes.length+"  "+ Rsa.bytesToHexString(commandBytes));
        BluetoothLeService.writeOTABootLoaderCommand(mOtaCharacteristic, commandBytes, true);
    }
}
