package com.quartz.climb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Client-side prototype controller. It is intentionally simple so it can be tuned later.
 */
public final class ClimbController {
    public static final double REACH = 2.6D;
    public static final double MOVE_SPEED = 0.085D;
    public static final double SURFACE_OFFSET = 0.34D;

    private final ClimbState state = new ClimbState();

    public ClimbState state() {
        return state;
    }

    public void toggle(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        if (state.climbing) {
            detach(player);
            return;
        }

        BlockHitResult hit = wallTrace(minecraft, player);
        if (hit.getType() != HitResult.Type.BLOCK || hit.getDirection() == Direction.DOWN || hit.getDirection() == Direction.UP) {
            return;
        }

        Direction face = hit.getDirection();
        state.start(hit.getLocation().x, hit.getLocation().y, hit.getLocation().z,
                face.getStepX(), face.getStepY(), face.getStepZ());
        player.setDeltaMovement(Vec3.ZERO);
        player.setOnGround(false);
    }

    public void tick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null || !state.climbing) return;

        if (!hasWallContact(minecraft, player)) {
            detach(player);
            return;
        }

        state.gripTicks++;
        if ((state.gripTicks % 12) == 0) state.leftHand = !state.leftHand;

        double up = 0.0D;
        if (minecraft.options.keyUp.isDown()) up += 1.0D;
        if (minecraft.options.keyDown.isDown()) up -= 1.0D;

        double side = 0.0D;
        if (minecraft.options.keyLeft.isDown()) side -= 1.0D;
        if (minecraft.options.keyRight.isDown()) side += 1.0D;

        Vec3 normal = new Vec3(state.wallNormalX, state.wallNormalY, state.wallNormalZ);
        Vec3 right = new Vec3(-normal.z, 0, normal.x);
        if (right.lengthSqr() < 1.0E-5) right = new Vec3(1, 0, 0);
        right = right.normalize();

        Vec3 motion = new Vec3(0, up * MOVE_SPEED, 0).add(right.scale(side * MOVE_SPEED));
        // Small inward correction keeps the player visually attached without forcing a vanilla ladder state.
        Vec3 target = player.position().add(motion).add(normal.scale(0.02D));
        player.setDeltaMovement(motion);
        player.setPos(target.x, target.y, target.z);

        if (minecraft.options.keyJump.isDown() || minecraft.options.keyShift.isDown()) {
            detach(player);
        }
    }

    public void detach(LocalPlayer player) {
        state.stop();
        player.setDeltaMovement(new Vec3(player.getDeltaMovement().x, 0.12D, player.getDeltaMovement().z));
    }

    private BlockHitResult wallTrace(Minecraft minecraft, LocalPlayer player) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(REACH));
        return minecraft.level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    private boolean hasWallContact(Minecraft minecraft, LocalPlayer player) {
        BlockPos pos = player.blockPosition();
        Direction[] faces = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction face : faces) {
            if (!minecraft.level.getBlockState(pos.relative(face)).isAir()) return true;
        }
        return false;
    }
}
