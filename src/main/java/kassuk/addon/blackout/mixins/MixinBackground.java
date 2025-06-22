package kassuk.addon.blackout.mixins;

import kassuk.addon.blackout.modules.Fog;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static meteordevelopment.meteorclient.systems.modules.Modules.get;

@Mixin(BackgroundRenderer.class)
public class MixinBackground {
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<net.minecraft.client.render.Fog> cir) {
        Fog fog = get().get(Fog.class);
        if (fog != null && fog.isActive() && fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            cir.setReturnValue(fog.getFog());
        }
    }
}
