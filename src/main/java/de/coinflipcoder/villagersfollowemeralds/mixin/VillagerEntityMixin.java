package de.coinflipcoder.villagersfollowemeralds.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity {

    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    // Inject the "tempt_range" attribute to VillagerEntity, needed for TemptGoal
    @Inject(method = "createVillagerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectTemptRangeAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
        builder.add(EntityAttributes.TEMPT_RANGE, 16.0D); // I pulled 16.0D out of my ass, same with 0.4D below
        cir.setReturnValue(builder);
    }

    // Inject TemptGoal
    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;Lnet/minecraft/registry/entry/RegistryEntry;)V", at = @At("TAIL"))
    private void injectGoal(EntityType<? extends VillagerEntity> entityType, World world, RegistryEntry<VillagerType> type, CallbackInfo ci) {
        this.goalSelector.add(2, new TemptGoal(this, 0.4D, Ingredient.ofItems(Items.EMERALD_BLOCK, Items.EMERALD_ORE, Items.DEEPSLATE_EMERALD_ORE, Items.EMERALD), false));
    }
}
