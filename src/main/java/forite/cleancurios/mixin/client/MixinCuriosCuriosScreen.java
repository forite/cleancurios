package forite.cleancurios.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.client.gui.CuriosScreen;

@Mixin(CuriosScreen.class)
public class MixinCuriosCuriosScreen {

    @Shadow @Final private static ResourceLocation CURIO_INVENTORY;
    @Unique private int firstBlitCall_slots;

    // Remove default y offset of Curio background
    @ModifyConstant(method = "lambda$renderBg$6", constant = @Constant(intValue = 4))
    private static int renderBg$lambda$removeCuriosScreenOffset(int constant) {
        return 0;
    }

    // Move Curio background closer to the inventory
    @ModifyConstant(method = "lambda$renderBg$6", constant = @Constant(intValue = -26))
    private static int renderBg$lambda$mergeCuriosScreen(int constant) {
        return -19;
    }

    // Special behavior: between 4-7 charm slots, move any past 4 to above the offhand slot
    @Redirect(method = "lambda$renderBg$6", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 0))
    private void renderBg$lambda$firstBlitCall(GuiGraphics instance, ResourceLocation img, int x, int y, int xTex, int yTex, int widthTex, int heightTex) {
        this.firstBlitCall_slots = (heightTex - 7) / 18;

        // if less than 5 slots, normal behavior
        // if more than 7 slots, normal behavior
        // if 5-7 slots, new behavior
        // In first call, all that needs to be done is change 5-7 slots to 4 slots
        if (this.firstBlitCall_slots < 8 && this.firstBlitCall_slots > 4) {
            heightTex = 79; // 7 + (4 * 18)
        }
        instance.blit(img, x, y, xTex, yTex, widthTex, heightTex);
    }

    @Redirect(method = "lambda$renderBg$6", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 1))
    private void renderBg$lambda$secondBlitCall(GuiGraphics instance, ResourceLocation img, int x, int y, int xTex, int yTex, int widthTex, int heightTex) {
        // Pre: firstBlitCall already was called, so slots is set correctly
        if (this.firstBlitCall_slots < 8 && this.firstBlitCall_slots > 4) {
            int top = y - ((this.firstBlitCall_slots * 18) + 7);
            int left = x + 19;
            y = top + 79;
            int slotsToRender = this.firstBlitCall_slots - 4;
            for (int i = 0; i < slotsToRender; i++) {
                instance.blit(CURIO_INVENTORY, left + 76, top + 7 + (i * 18), 138, 0, 18, 18);
            }
        }
        instance.blit(img, x, y, xTex, yTex, widthTex, heightTex);
    }

}
