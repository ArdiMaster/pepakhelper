package me.ardimaster.pepak.coretweaker.transformers;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class WailaSRFixingTransformer implements IClassTransformer {
    private static final String[] classesToTransform = {
            // "com.github.abrarsyed.secretroomsmod.common.SecretRooms"
            // "com.github.abrarsyed.secretroomsmod.client.waila.WailaProvider"
            "com.github.abrarsyed.secretroomsmod.common.OwnershipManager"
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
                        // transformMainSecretRooms(node);
                        // transformWailaProvider(node);
                        transformOwnershipManager(node);
                }
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                node.accept(classWriter);
                return classWriter.toByteArray();
            } catch (Exception e) {
                log(Level.ERROR, "Unable to transform class " + transformedName + ", it will be loaded without modifications.");
                return inputClass;
            }
        } else {
            return inputClass;
        }
    }

    private void transformOwnershipManager(ClassNode classOwnershipManager) {
        for (MethodNode method : classOwnershipManager.methods) {
            if (method.name.equals("isOwner") && method.desc.equals("(Ljava/util/UUID;Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;)Z")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == Opcodes.ALOAD) {
                        if (((VarInsnNode) instruction).var == 0 && instruction.getNext().getOpcode() == Opcodes.ALOAD) {
                            targetNode = instruction;
                            break;
                        }
                    }
                }

                if (targetNode != null) {
                    for (int i = 0; i < 4; i++) {
                        targetNode = targetNode.getNext();
                        method.instructions.remove(targetNode.getPrevious());
                    }
                    method.instructions.insertBefore(targetNode, new InsnNode(Opcodes.ICONST_0));
                    log(Level.INFO, "    " + method.name + method.desc + " - Transformed");
                } else {
                    log(Level.ERROR, "Unable to find target instruction in method "+ method.name + method.desc + " - it will be loaded without modifications.");
                }
            }
        }
    }

    /* private void transformWailaProvider(ClassNode classWailaProvider) {
        for (MethodNode method : classWailaProvider.methods) {
            if (method.name.equals("getWailaStack") &&
                    method.desc.equals("(Lmcp/mobius/waila/api/IWailaDataAccessor;Lmcp/mobius/waila/api/IWailaConfigHandler;)Lnet/minecraft/item/ItemStack;")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == Opcodes.ALOAD) {
                        if (((VarInsnNode) instruction).var == 1 && instruction.getNext().getOpcode() == Opcodes.INVOKEINTERFACE) {
                            MethodInsnNode nextInstruction = (MethodInsnNode) instruction.getNext();
                            if (nextInstruction.name.equals("getPlayer") && nextInstruction.desc.equals("()Lnet/minecraft/entity/player/EntityPlayer;") &&
                                    nextInstruction.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                                targetNode = instruction;
                                break;
                            }
                        }
                    }
                }

                if (targetNode != null) {
                    for (int i = 0; i < 5; i++) {
                        targetNode = targetNode.getNext();
                        method.instructions.remove(targetNode.getPrevious());
                    }
                    method.instructions.insertBefore(targetNode, new InsnNode(Opcodes.ICONST_0));
                    log(Level.INFO, "    " + method.name + method.desc + " - Transformed");
                } else {
                    log(Level.ERROR, "Unable to find target instruction in method "+ method.name + method.desc + " - it will be loaded without modifications.");
                }
            } else if (method.name.equals("getWailaBody") &&
                    method.desc.equals("(Lnet/minecraft/item/ItemStack;Ljava/util/List;Lmcp/mobius/waila/api/IWailaDataAccessor;Lmcp/mobius/waila/api/IWailaConfigHandler;)Ljava/util/List;")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == Opcodes.ALOAD) {
                        if (((VarInsnNode) instruction).var == 3 && instruction.getNext().getOpcode() == Opcodes.INVOKEINTERFACE) {
                            MethodInsnNode nextInstruction = (MethodInsnNode) instruction.getNext();
                            if (nextInstruction.name.equals("getPlayer") &&
                                    nextInstruction.desc.equals("()Lnet/minecraft/entity/player/EntityPlayer;") &&
                                    nextInstruction.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                                targetNode = instruction;
                                break;
                            }
                        }
                    }
                }

                if (targetNode != null) {
                    for (int i = 0; i < 5; i++) {
                        targetNode = targetNode.getNext();
                        method.instructions.remove(targetNode.getPrevious());
                    }
                    method.instructions.insertBefore(targetNode, new InsnNode(Opcodes.ICONST_0));
                    log(Level.INFO, "    " + method.name + method.desc + " - Transformed");
                } else {
                    log(Level.ERROR, "Unable to find target instruction in method "+ method.name + method.desc + " - it will be loaded without modifications.");
                }
            }
        }
    } */

    /* private void transformMainSecretRooms(ClassNode classSecretRooms) {
        for (MethodNode method : classSecretRooms.methods) {
            if (method.name.equals("load") && method.desc.equals("(Lcpw/mods/fml/common/event/FMLInitializationEvent;)V")) {
                AbstractInsnNode targetNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == Opcodes.LDC) {
                        if (((LdcInsnNode) instruction).cst.equals("Waila")) {
                            targetNode = instruction;
                            break;
                        }
                    }
                }

                if (targetNode != null) {
                    for (int i = 0; i < 5; i++) {
                        targetNode = targetNode.getNext();
                        method.instructions.remove(targetNode.getPrevious());
                    }
                    log(Level.INFO, "    " + method.name + method.desc + " - Transformed");
                } else {
                    return;
                }
            }
        }
    } */

    private void log(Level level, String message) {
        FMLRelaunchLog.log("PEpakCoreTweaker:WailaSRFixer", level, message);
    }
}
