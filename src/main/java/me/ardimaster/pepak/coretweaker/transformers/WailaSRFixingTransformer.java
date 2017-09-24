package me.ardimaster.pepak.coretweaker.transformers;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;

public class WailaSRFixingTransformer implements IClassTransformer {
    private static final String[] classesToTransform = {
            "com.github.abrarsyed.secretroomsmod.common.SecretRooms"
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
                        transformMainSecretRooms(node);
                }
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                node.accept(classWriter);
                return classWriter.toByteArray();
            } catch (Exception e) {
                log(Level.FATAL, "Unable to transform class " + transformedName);
                e.printStackTrace();
                return inputClass;
            }
        } else {
            return inputClass;
        }
    }

    private void transformMainSecretRooms(ClassNode classSecretRooms) {
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
                        method.instructions.remove(targetNode);
                    }
                    log(Level.INFO, "    " + method.name + method.desc + " - Transformed");
                } else {
                    return;
                }
            }
        }
    }

    private void log(Level level, String message) {
        FMLRelaunchLog.log("PEpakCoreTweaker:WailaSRFixer", level, message);
    }
}
