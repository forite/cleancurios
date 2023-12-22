package forite.cleancurios.client;

import forite.cleancurios.CleanCuriosMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;

@Mod.EventBusSubscriber(modid = CleanCuriosMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    // Intercepts Survival Inventory Open and turns it into Curio Screen open
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.isWindowActive()) {
            return;
        }

        if (mc.options.keyInventory.consumeClick()) {
            if (mc.player.isCreative()) {
                // Let vanilla handle the click instead
                KeyMapping.click(mc.options.keyInventory.getKey());
            } else {
                // Open Curio Screen
                NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                        new CPacketOpenCurios(ItemStack.EMPTY));
            }
        }
    }
}
