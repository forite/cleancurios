package forite.cleancurios.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.client.gui.CuriosButton;

@Mixin(CuriosButton.class)
public class MixinCuriosCuriosButton {

    // TODO: Switch to an implementation that actually fully removes the button

    // Cancels custom Curios Button event handling
    @Inject(method = "lambda$new$0", at = @At("HEAD"), cancellable = true)
    private static void new$lambda$cancelOnPressLambda(AbstractContainerScreen parentGui, Button button, CallbackInfo ci) {
        ci.cancel();
    }

    // Cancels custom Curios Button event handling
    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void renderWidget$cancelRenderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ci.cancel();
    }
}
