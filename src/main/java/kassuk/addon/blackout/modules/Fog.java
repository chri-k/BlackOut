package kassuk.addon.blackout.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import kassuk.addon.blackout.BlackOut;
import kassuk.addon.blackout.BlackOutModule;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.client.render.FogShape;

/**
 * @author OLEPOSSU
 */

public class Fog extends BlackOutModule {
    public Fog() {
        super(BlackOut.BLACKOUT, "Fog", "Customizable fog.");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<FogShape> shape = sgGeneral.add(new EnumSetting.Builder<FogShape>()
        .name("Shape")
        .description("Fog shape.")
        .defaultValue(FogShape.SPHERE)
        .build()
    );
    public final Setting<Double> distance = sgGeneral.add(new DoubleSetting.Builder()
        .name("start")
        .description("How far away should the fog start rendering.")
        .defaultValue(25)
        .noSlider()
        .build()
    );
    public final Setting<Integer> fading = sgGeneral.add(new IntSetting.Builder()
        .name("fading")
        .description("How quickly the fog fades")
        .defaultValue(200)
        .sliderRange(1, 1000)
        .build()
    );
    public final Setting<Double> thickness = sgGeneral.add(new DoubleSetting.Builder()
        .name("thickness")
        .description(".")
        .defaultValue(40)
        .range(1, 100)
        .sliderRange(1, 100)
        .onChanged(this::updateThicknessFactor)
        .build()
    );
    public final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .description("Color of the fog.")
        .defaultValue(new SettingColor(9, 4, 103, 255))
        .build()
    );

    // makes the effect of the slider feel more linear
    private double thicknessFactor;
    private void updateThicknessFactor(double thickness) {
        thicknessFactor = thickness * thickness / 10000.0f;
    }

    public net.minecraft.client.render.Fog getFog() {
        return new net.minecraft.client.render.Fog(
            distance.get().floatValue(),
            Math.max(distance.get().floatValue(), 10) * fading.get(),
            shape.get(),
            color.get().r,
            color.get().g,
            color.get().b,
            (int)(color.get().a * thicknessFactor)
        );
    }
}
