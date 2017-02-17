package com.noctarius.lightify;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LightifyZone extends LightifyLuminary {

    private final int zoneId;
    private final byte[] address;

    private final List<LightifyLuminary> luminaries = new CopyOnWriteArrayList<>();

    LightifyZone(LightifyLink lightifyLink, String name, int zoneId) {
        super(lightifyLink, name);
        this.zoneId = zoneId;
        this.address = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putShort((short) zoneId).array();
    }

    @Override
    public byte[] address() {
        return address;
    }

    @Override
    public boolean isPowered() {
        boolean powered = true;
        for (LightifyLuminary luminary : luminaries) {
            if (!luminary.isPowered()) {
                powered = false;
                break;
            }
        }
        return powered;
    }

    @Override
    public short getTemperature() {
        short temperature = -1;
        for (LightifyLuminary luminary : luminaries) {
            if (temperature == -1) {
                temperature = luminary.getTemperature();
            } else if (temperature != luminary.getTemperature()) {
                temperature = 2000;
                break;
            }
        }
        return temperature;
    }

    @Override
    public byte getLuminance() {
        byte luminance = -1;
        for (LightifyLuminary luminary : luminaries) {
            if (luminance == -1) {
                luminance = luminary.getLuminance();
            } else if (luminance != luminary.getLuminance()) {
                luminance = 100;
                break;
            }
        }
        return luminance;
    }

    @Override
    public byte[] getRGB() {
        byte[] rgb = null;
        for (LightifyLuminary luminary : luminaries) {
            if (rgb == null) {
                rgb = luminary.getRGB();
            } else if (Arrays.equals(rgb, luminary.getRGB())) {
                rgb = new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff};
                break;
            }
        }
        return rgb;
    }

    public int getZoneId() {
        return zoneId;
    }

    @Override
    public String toString() {
        return "com.noctarius.lightify.LightifyZone{" + "zoneId=" + zoneId + ", address=" + Arrays.toString(address) + ", luminaries=" + luminaries
                + ", super=" + super.toString() + '}';
    }

    @Override
    byte typeFlag() {
        return 0x02;
    }

    void addDevice(LightifyLuminary luminary) {
        luminaries.add(luminary);
    }
}
