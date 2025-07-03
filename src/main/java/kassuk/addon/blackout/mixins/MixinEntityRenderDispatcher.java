package kassuk.addon.blackout.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import kassuk.addon.blackout.managers.Managers;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher<E extends Entity, S extends EntityRenderState> {
    @ModifyArg(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private S inject(S state, @Local(argsOnly = true) E entity, @Local(argsOnly = true) float tickDelta) {
        if (entity == mc.player) Managers.ROTATION.modifyRenderState((PlayerEntityRenderState) state, tickDelta);
        return state;
    }
}
