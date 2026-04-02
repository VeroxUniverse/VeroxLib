package net.veroxuniverse.veroxlib.sanity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SanityData(float value) {
    public static final Codec<SanityData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.FLOAT.fieldOf("value").forGetter(SanityData::value))
                    .apply(instance, SanityData::new));

    public SanityData add(float amount) {
        return new SanityData(Math.max(0, Math.min(100, this.value + amount)));
    }
}