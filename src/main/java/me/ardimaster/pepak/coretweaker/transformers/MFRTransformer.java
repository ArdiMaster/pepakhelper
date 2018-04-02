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
                log(Level.DEBUG, "PEpCT: Patching class " + transformedName);

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
                log(Level.INFO, "PEpCT: Done patching class " + transformedName);
                return classWriter.toByteArray();
            } catch (Exception e) {
                log(Level.WARN, "Potential problem: PEpakCoreTweaker was unable to patch " + transformedName);
                e.printStackTrace();
                return inputClass;
            }
        }
        return inputClass;
    }

    private void transformFactoryFluid(ClassNode factoryFluidClass) {
        for (MethodNode method : factoryFluidClass.methods) {
            if (method.name.equals("func_149670_a") && method.desc.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V")) {
                log(Level.INFO, "   Patching method onEntityCollidedWithBlock (func_149670_a)");
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
                    InsnList addInsnList = new InsnList();
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    addInsnList.add(new FieldInsnNode(Opcodes.GETFIELD, "powercrystals/minefactoryreloaded/block/fluid/BlockFactoryFluid", "fluidName", "Ljava/lang/String;"));
                    addInsnList.add(new VarInsnNode(Opcodes.ALOAD, 7));
                    addInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/ardimaster/pepak/helper/hooks/MFRHooks", "shouldFluidEffectsApply", "(Ljava/lang/String;Lnet/minecraft/entity/EntityLivingBase;)Z", false));
                    LabelNode continueLabel = new LabelNode(new Label());
                    addInsnList.add(new JumpInsnNode(Opcodes.IFNE, continueLabel));
                    addInsnList.add(new InsnNode(Opcodes.RETURN));
                    addInsnList.add(continueLabel);
                    addInsnList.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"net/minecraft/entity/EntityLivingBase"}, 0, null));
                    method.instructions.insertBefore(targetNode, addInsnList);
                    log(Level.DEBUG, "   Successfully patched func_149670_a(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V");
                } else {
                    log(Level.WARN, "   Unable to patch func_149670_a(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V (target not found)!");
                }
            }
        }
    }

    private void transformSludgeBoiler(ClassNode sludgeBoilerClass) {
        for (MethodNode method : sludgeBoilerClass.methods) {
            if (method.name.equals("activateMachine") && method.desc.equals("()Z")) {
                log(Level.INFO, "   Patching method activateMachine");
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

                    LabelNode newStuffStartLabel = new LabelNode(new Label());
                    LabelNode newStuffDoneLabel = new LabelNode(new Label());
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

                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/ardimaster/pepak/helper/hooks/MFRHooks", "shouldSludgeBoilerEffectsApply", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));
                    LabelNode continueLabel = new LabelNode(new Label());
                    list.add(new JumpInsnNode(Opcodes.IFNE, continueLabel));
                    list.add(new JumpInsnNode(Opcodes.GOTO, newStuffStartLabel));
                    list.add(continueLabel);
                    list.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"net/minecraft/entity/EntityLivingBase"}, 0, null));

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
                    log(Level.DEBUG, "   Successfully patched activateMachine()Z");
                } else {
                    log(Level.WARN, "   Unable to patch activateMachine()Z (target not found)!");
                }
            }
        }
    }

    private void log(Level level, String message) {
        FMLRelaunchLog.log("PEpakCoreTweaker:MFR", level, message);
    }
}
