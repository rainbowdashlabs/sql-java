package de.chojo.simplecoins.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UUIDConverterTest {
    @Test
    public void shouldGet16BytesFromAUUID() {
        UUID uuid = UUID.randomUUID();

        byte[] result = UUIDConverter.convert(uuid);
        // Expected result to be a byte array w/16 elements.
        Assertions.assertEquals(16, result.length);
    }

    @Test
    public void shouldReconstructSameUUIDFromByteArray() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = UUIDConverter.convert(uuid);
        UUID reconstructedUuid = UUIDConverter.convert(bytes);

        Assertions.assertEquals(uuid, reconstructedUuid);
    }

    @Test
    public void shouldNotGenerateTheSameUUIDFromBytes() {
        UUID uuid = UUID.fromString("9f881758-0b4a-4eaa-b59f-b6dea0934223");

        byte[] result = UUIDConverter.convert(uuid);
        UUID newUuid = UUID.nameUUIDFromBytes(result);

        Assertions.assertFalse(uuid.equals(newUuid));
    }
}