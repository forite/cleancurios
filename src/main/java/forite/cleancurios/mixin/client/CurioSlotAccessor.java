package forite.cleancurios.mixin.client;

import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.theillusivec4.curios.common.inventory.CurioSlot;

@Mixin(CurioSlot.class)
public interface CurioSlotAccessor {
    @Accessor()
    NonNullList<Boolean> getRenderStatuses();
}
