package kassuk.addon.blackout.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kassuk.addon.blackout.managers.Managers;
import meteordevelopment.meteorclient.mixininterface.IClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * @author OLEPOSSU
 */

@SuppressWarnings("DataFlowIssue")
public class BOInvUtils {
    private static int[] slots;
    public static int pickSlot = -1;

    // Updated by H1ggsK
    public static boolean pickSwitch(int slot) {
        if (slot >= 0) {
            Managers.HOLDING.modifyStartTime = System.currentTimeMillis();
            pickSlot = slot;

            // Obtain the container ID and revision from the player's current screen handler.
            int syncId = mc.player.currentScreenHandler.syncId;
            // Some implementations offer a revision getter; if not, you might default to 0.
            int revision = mc.player.currentScreenHandler.getRevision();

            // Define the click parameters:
            // button: 0 for primary click (adjust if you need a different click type)
            int button = 0;
            // actionType: use SlotActionType.PICKUP for a normal click (or change as needed)
            SlotActionType actionType = SlotActionType.PICKUP;

            // Get the current item stack from the slot; this might be the item you’re about to move.
            ItemStack stack = mc.player.currentScreenHandler.getSlot(slot).getStack();

            // Create an empty map for modified stacks.
            // If your use-case requires sending updated stacks for other slots, populate this accordingly.
            Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();

            // Create and send the ClickSlotC2SPacket with the specified parameters.
            ClickSlotC2SPacket packet = new ClickSlotC2SPacket(syncId, revision, slot, button, actionType, stack, modifiedStacks);
            mc.getNetworkHandler().sendPacket(packet);

            return true;
        }
        return false;
    }

    // Updated by H1ggsK
    public static void pickSwapBack() {
        if (pickSlot >= 0) {
            // Obtain the container (screen handler) ID and revision.
            int syncId = mc.player.currentScreenHandler.syncId;
            // Use the current revision, or default to 0 if not available.
            int revision = mc.player.currentScreenHandler.getRevision();

            // Set click parameters.
            int button = 0; // Typically 0 for left-click; adjust as needed.
            SlotActionType actionType = SlotActionType.PICKUP;

            // Retrieve the item stack from the stored pickSlot.
            ItemStack stack = mc.player.currentScreenHandler.getSlot(pickSlot).getStack();

            // Create an empty map for modified stacks (populate if needed).
            Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();

            // Create and send the ClickSlotC2SPacket with the specified parameters.
            ClickSlotC2SPacket packet = new ClickSlotC2SPacket(syncId, revision, pickSlot, button, actionType, stack, modifiedStacks);
            mc.getNetworkHandler().sendPacket(packet);

            // Reset pickSlot after sending the packet.
            pickSlot = -1;
        }
    }

    // Credits to rickyracuun
    public static boolean invSwitch(int slot) {
        if (slot >= 0) {
            ScreenHandler handler = mc.player.currentScreenHandler;
            Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
            stack.put(slot, handler.getSlot(slot).getStack());

            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId,
                handler.getRevision(), PlayerInventory.MAIN_SIZE + Managers.HOLDING.slot,
                slot, SlotActionType.SWAP, handler.getSlot(slot).getStack(), stack)
            );
            ((IClientPlayerInteractionManager) mc.interactionManager).meteor$syncSelected();
            slots = new int[]{slot, Managers.HOLDING.slot};
            return true;
        }
        return false;
    }

    public static void swapBack() {
        ScreenHandler handler = mc.player.currentScreenHandler;
        Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
        stack.put(slots[0], handler.getSlot(slots[0]).getStack());

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId,
            handler.getRevision(), PlayerInventory.MAIN_SIZE + slots[1],
            slots[0], SlotActionType.SWAP, handler.getSlot(slots[0]).getStack().copy(), stack)
        );
        ((IClientPlayerInteractionManager) mc.interactionManager).meteor$syncSelected();
    }
}
