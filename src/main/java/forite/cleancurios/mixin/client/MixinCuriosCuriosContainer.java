package forite.cleancurios.mixin.client;

import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

@Mixin(CuriosContainer.class)
public abstract class MixinCuriosCuriosContainer {

    @Unique private int local$slotsAdded;

    // Remove default offset of Curio slots from initial
    @ModifyConstant(method = "lambda$new$0", constant = @Constant(intValue = 12))
    private int new$lambda$removeOffset(int constant) {
        return 8;
    }

    // Remove default offset of Curio slots from scroll
    @ModifyConstant(method = "lambda$scrollToIndex$2", constant = @Constant(intValue = 12))
    private int scrollTo$lambda$removeOffset(int constant) {
        return 8;
    }

    @Inject(method = "lambda$new$0", at = @At("HEAD"))
    private void new$lambda$setSlotsAdded(ICuriosItemHandler curios, CallbackInfo ci) {
        local$slotsAdded = getSlotCount(curios);
    }

    // Special behavior: between 4-7 charm slots, move any past 4 to above the offhand slot
    @Redirect(method = "lambda$new$0", at = @At(value = "INVOKE", target = "top/theillusivec4/curios/common/inventory/container/CuriosContainer.addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;"))
    private Slot new$lambda$specialCaseCurios(CuriosContainer instance, Slot slot) {
        return ((AbstractContainerMenuAccessor)instance).addSlot$access(getSlot(instance, slot));
    }

    @Inject(method = "lambda$scrollToIndex$2", at = @At("HEAD"))
    private void scrollToIndex$lambda$setSlotsAdded(int indexIn, ICuriosItemHandler curios, CallbackInfo ci) {
        local$slotsAdded = getSlotCount(curios);
    }

    // Special behavior: between 4-7 charm slots, move any past 4 to above the offhand slot
    @Redirect(method = "lambda$scrollToIndex$2", at = @At(value = "INVOKE", target = "top/theillusivec4/curios/common/inventory/container/CuriosContainer.addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;"))
    private Slot scrollToIndex$lambda$specialCaseCurios(CuriosContainer instance, Slot slot) {
        return ((AbstractContainerMenuAccessor)instance).addSlot$access(getSlot(instance, slot));
    }

    private int getSlotCount(ICuriosItemHandler curios) {
        int slots = 0;
        for (String id : curios.getCurios().keySet()) {
            IDynamicStackHandler handler = curios.getCurios().get(id).getStacks();
            for (int i = 0; i < handler.getSlots(); i++) {
                slots++;
            }
        }
        return slots;
    }

    @NotNull
    private Slot getSlot(CuriosContainer instance, Slot slot) {
        CurioSlot curioSlot = (CurioSlot) slot;
        int slotIndex = (slot.y - 8) / 18;

        // if less than 5 slots, normal behavior
        // if more than 7 slots, normal behavior
        // if 5-7 slots, new behavior
        int x;
        int y = slot.y;
        IDynamicStackHandler handler = (IDynamicStackHandler) curioSlot.getItemHandler();
        if (slotIndex > 3 && slotIndex < 7 && local$slotsAdded > 4 && local$slotsAdded < 8) {
            x = 77;
            y = 8 + (slotIndex - 4) * 18;
        } else {
            x = -11;
        }
        return new CurioSlot(instance.player, (IDynamicStackHandler) curioSlot.getItemHandler(), slot.index, curioSlot.getIdentifier(), x, y,
                ((CurioSlotAccessor)curioSlot).getRenderStatuses(), false);
    }
}

