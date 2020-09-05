package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.utils.ResourceLocation;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataCube {

    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("data_cube", ResourceLocation.SPRITE);

    public static DataCube createRandomCube() {
        return new DataCube(true, 0);
    }

    public static DataCube createDataCube(int value) {
        return new DataCube(false, value);
    }

    private int value;
    private final boolean randomValue;

    private DataCube(boolean random, int value) {
        if (random) {
            this.value = randomValue();
            randomValue = true;
        } else {
            this.value = value;
            randomValue = false;
        }
    }

    public DataCube(DataCube cube) {
        this.randomValue = cube.randomValue;

        if (randomValue) {
            value = randomValue();
        } else {
            value = cube.getValue();
        }
    }

    private int randomValue() {
        return ThreadLocalRandom.current().nextInt(-99, 100);
    }

    public void add(int v) {
        setValue(value + v);
    }

    public void sub(int v) {
        setValue(value - v);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        // bad
        if (value > 99) {
            do {
                int v2 = value - 99;

                this.value = -100 + v2;
            } while (this.value > 99);
        } else if (value < -99) {
            do {
                int v2 = +value - 99;

                this.value = 100 - v2;
            } while (this.value < -99);
        }
    }

    public ResourceLocation getResourceLocation() {
        return RESOURCE_LOCATION;
    }
}