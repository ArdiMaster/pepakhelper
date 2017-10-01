package me.ardimaster.pepak.coretweaker.transformers;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class MFRTransformer implements IClassTransformer {
    private static final String[] classesToTransform = {
            "powercrystals.minefactoryreloaded.block.fluid.BlockFactoryFluid",
            "powercrystals.minefactoryreloaded.tile.machine.TileEntitySludgeBoiler"
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] inputClass) {
        int index = Arrays.asList(classesToTransform).indexOf(transformedName);
        if (index != -1) {
            try {
                log(Level.INFO, "Transforming class " + transformedName);

                ClassNode node = new ClassNode();
                ClassReader classReader = new ClassReader(inputClass);
                classReader.accept(node, 0);
                switch (index) {
                    case 0:
                        transformFactoryFluid(node);
                        break;
                    case 1:
                        transformSludgeBoiler(node);
                }
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                node.accept(classWriter);
                return classWriter.toByteArray();
            } catch (Exception e) {
                log(Level.WARN, "Unable to transform class " + transformedName + ", it will be loaded without modifications.");
                e.printStackTrace();
                return inputClass;
            }
        }
        return inputClass;
    }

    private void transformFactoryFluid(ClassNode factoryFluidClass) {
        for (MethodNode method : factoryFluidClass.methods) {
            if (method.name.equals("func_149670_a") && method.desc.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instuction : method.instructions.toArray()) {
                    if (instuction.getOpcode() == Opcodes.ALOAD) {
                        if (((VarInsnNode) instuction).var == 0 && instuction.getNext().getOpcode() == Opcodes.GETSTATIC) {
                            targetNode = instuction;
                            break;
                        }
                    }
                }

                if (targetNode !=  null) {
                    LabelNode armorTryCatchStartLabel = new LabelNode(new Label());
                    LabelNode armorTryCatchEndLabel = new LabelNode(new Label());
                    LabelNode armorTryCatchHandlerLabel = new LabelNode(new Label());
                    LabelNode newStuffStartLabel = new LabelNode(new Label());
                    LabelNode newStuffDoneLabel = new LabelNode(new Label());
                    method.tryCatchBlocks.add(new TryCatchBlockNode(armorTryCatchStartLabel, armorTryCatchEndLabel, armorTryCatchHandlerLabel, "java/lang/NullPointerException"));

                    InsnList addInsnList = new InsnList();
                    addInsnList.add(newStuffStartLabel);
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 7));
                    addInsnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/entity/player/EntityPlayer"));
                    LabelNode newStuffLabel1 = new LabelNode(new Label());
                    addInsnList.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel1));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 7));
                    addInsnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/entity/player/EntityPlayer"));
                    addInsnList.add(new VarInsnNode(Opcodes.ASTORE, 8));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 8));
                    addInsnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "field_71071_by",
                            "Lnet/minecraft/entity/player/InventoryPlayer;"));
                    addInsnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", "field_70460_b", "[Lnet/minecraft/item/ItemStack;"));
                    addInsnList.add(new VarInsnNode(Opcodes.ASTORE, 9));
                    addInsnList.add(armorTryCatchStartLabel);
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 9));
                    addInsnList.add(new InsnNode(Opcodes.ICONST_0));
                    addInsnList.add(new InsnNode(Opcodes.AALOAD));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    addInsnList.add(new LdcInsnNode("ic2.itemArmorRubBoots"));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    LabelNode newStuffLabel2 = new LabelNode(new Label());
                    addInsnList.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 9));
                    addInsnList.add(new InsnNode(Opcodes.ICONST_1));
                    addInsnList.add(new InsnNode(Opcodes.AALOAD));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    addInsnList.add(new LdcInsnNode("ic2.itemArmorHazmatLeggings"));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    addInsnList.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 9));
                    addInsnList.add(new InsnNode(Opcodes.ICONST_2));
                    addInsnList.add(new InsnNode(Opcodes.AALOAD));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    addInsnList.add(new LdcInsnNode("ic2.itemArmorHazmatChestplate"));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    addInsnList.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 9));
                    addInsnList.add(new InsnNode(Opcodes.ICONST_3));
                    addInsnList.add(new InsnNode(Opcodes.AALOAD));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    addInsnList.add(new LdcInsnNode("ic2.itemArmorHazmatHelmet"));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    addInsnList.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    addInsnList.add(armorTryCatchEndLabel);
                    addInsnList.add(new InsnNode(Opcodes.RETURN));
                    addInsnList.add(newStuffLabel2);
                    addInsnList.add(new FrameNode(Opcodes.F_APPEND, 3, new Object[]{"net/minecraft/entity/EntityLivingBase",
                            "net/minecraft/entity/player/EntityPlayer", "[Lnet/minecraft/item/ItemStack;"}, 0, null));
                    addInsnList.add(new JumpInsnNode(Opcodes.GOTO, newStuffLabel1));
                    addInsnList.add(armorTryCatchHandlerLabel);
                    addInsnList.add(new FrameNode(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/NullPointerException"}));
                    addInsnList.add(new VarInsnNode(Opcodes.ASTORE, 10));
                    addInsnList.add(newStuffLabel1);
                    addInsnList.add(new FrameNode(Opcodes.F_CHOP, 2, null, 0, null));
                    addInsnList.add(newStuffDoneLabel);
                    method.instructions.insertBefore(targetNode, addInsnList);
                    log(Level.INFO, "   Transformed method func_14670_a(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
                } else {
                    log(Level.WARN, "   Could not find target instruction in method func_149670_a, it will be loaded without modification");
                }
            }
        }
    }

    private void transformSludgeBoiler(ClassNode sludgeBoilerClass) {
        for (MethodNode method : sludgeBoilerClass.methods) {
            if (method.name.equals("activateMachine") && method.desc.equals("()Z")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instuction : method.instructions.toArray()) {
                    if (instuction.getOpcode() == Opcodes.ASTORE) {
                        if (((VarInsnNode) instuction).var == 2 && instuction.getPrevious().getOpcode() == Opcodes.INVOKEINTERFACE) {
                            MethodInsnNode prevInsn = (MethodInsnNode) instuction.getPrevious();
                            if (prevInsn.owner.equals("java/util/List") && prevInsn.name.equals("iterator") && prevInsn.getPrevious().getOpcode() == Opcodes.ALOAD) {
                                targetNode = instuction.getNext();
                                break;
                            }
                        }
                    }
                }

                if (targetNode != null) {
                    for (int i = 0; i < 37; i++) {
                        targetNode = targetNode.getNext();
                        method.instructions.remove(targetNode.getPrevious());
                    }
                    LabelNode armorTryCatchStartLabel = new LabelNode(new Label());
                    LabelNode armorTryCatchEndLabel = new LabelNode(new Label());
                    LabelNode armorTryCatchHandlerLabel = new LabelNode(new Label());
                    LabelNode armorTryCatchDoneLabel = new LabelNode(new Label());
                    LabelNode newStuffStartLabel = new LabelNode(new Label());
                    LabelNode newStuffDoneLabel = new LabelNode(new Label());
                    method.tryCatchBlocks.add(new TryCatchBlockNode(armorTryCatchStartLabel, armorTryCatchEndLabel, armorTryCatchHandlerLabel, "java/lang/NullPointerException"));
                    InsnList list = new InsnList();
                    list.add(newStuffStartLabel);
                    list.add(new FrameNode(Opcodes.F_APPEND, 2, new Object[]{"java/util/List", "java/util/Iterator"}, 0, null));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Iterator",
                            "hasNext", "()Z", true));
                    LabelNode newStuffLabel1 = new LabelNode(new Label());
                    list.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel1));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true));
                    list.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/entity/EntityLivingBase"));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 3));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/entity/player/EntityPlayer"));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, armorTryCatchDoneLabel));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    list.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/entity/player/EntityPlayer"));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 4));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 4));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "field_71071_by",
                            "Lnet/minecraft/entity/player/InventoryPlayer;"));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", "field_70460_b", "[Lnet/minecraft/item/ItemStack;"));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 5));
                    list.add(armorTryCatchStartLabel);
                    list.add(new VarInsnNode(Opcodes.ALOAD, 5));
                    list.add(new InsnNode(Opcodes.ICONST_0));
                    list.add(new InsnNode(Opcodes.AALOAD));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    list.add(new LdcInsnNode("ic2.itemArmorRubBoots"));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    LabelNode newStuffLabel2 = new LabelNode(new Label());
                    list.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 5));
                    list.add(new InsnNode(Opcodes.ICONST_1));
                    list.add(new InsnNode(Opcodes.AALOAD));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    list.add(new LdcInsnNode("ic2.itemArmorHazmatLeggings"));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 5));
                    list.add(new InsnNode(Opcodes.ICONST_2));
                    list.add(new InsnNode(Opcodes.AALOAD));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    list.add(new LdcInsnNode("ic2.itemArmorHazmatChestplate"));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 5));
                    list.add(new InsnNode(Opcodes.ICONST_3));
                    list.add(new InsnNode(Opcodes.AALOAD));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77977_a", "()Ljava/lang/String;", false));
                    list.add(new LdcInsnNode("ic2.itemArmorHazmatHelmet"));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, newStuffLabel2));
                    list.add(armorTryCatchEndLabel);
                    list.add(new JumpInsnNode(Opcodes.GOTO, newStuffStartLabel));
                    list.add(newStuffLabel2);
                    list.add(new FrameNode(Opcodes.F_APPEND, 3, new Object[]{"net/minecraft/entity/EntityLivingBase",
                            "net/minecraft/entity/player/EntityPlayer","[Lnet/minecraft/item/ItemStack;"}, 0, null));
                    list.add(new JumpInsnNode(Opcodes.GOTO, armorTryCatchDoneLabel));
                    list.add(armorTryCatchHandlerLabel);
                    list.add(new FrameNode(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/NullPointerException"}));
                    list.add(new VarInsnNode(Opcodes.ASTORE, 6));
                    list.add(armorTryCatchDoneLabel);
                    list.add(new FrameNode(Opcodes.F_CHOP, 2, null, 0, null));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    list.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/potion/PotionEffect"));
                    list.add(new InsnNode(Opcodes.DUP));
                    list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/potion/Potion", "field_76438_s", "Lnet/minecraft/potion/Potion;"));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/potion/Potion", "field_76415_H", "I"));
                    list.add(new IntInsnNode(Opcodes.SIPUSH, 400));
                    list.add(new InsnNode(Opcodes.ICONST_0));
                    list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "func_70690_d",
                            "(Lnet/minecraft/potion/PotionEffect;)V", false));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    list.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/potion/PotionEffect"));
                    list.add(new InsnNode(Opcodes.DUP));
                    list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/potion/Potion", "field_76436_u", "Lnet/minecraft/potion/Potion;"));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/potion/Potion", "field_76415_H", "I"));
                    list.add(new IntInsnNode(Opcodes.BIPUSH, 120));
                    list.add(new InsnNode(Opcodes.ICONST_0));
                    list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "func_70690_d",
                            "(Lnet/minecraft/potion/PotionEffect;)V", false));
                    list.add(new JumpInsnNode(Opcodes.GOTO, newStuffStartLabel));
                    list.add(newStuffLabel1);
                    list.add(new FrameNode(Opcodes.F_CHOP, 2, null, 0, null));
                    list.add(newStuffDoneLabel);
                    method.instructions.insertBefore(targetNode, list);
                    log(Level.INFO, "   Transformed method activateMachine()Z");
                } else {
                    log(Level.WARN, "   Could not find target instruction in method activateMachine()Z, it will be loaded without modification");
                }
            }
        }
    }

    private void log(Level level, String message) {
        FMLRelaunchLog.log("PEpakCoreTweaker:MFR", level, message);
    }
}
