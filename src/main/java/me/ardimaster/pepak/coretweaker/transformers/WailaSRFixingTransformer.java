package me.ardimaster.pepak.coretweaker.transformers;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.Arrays;

public class WailaSRFixingTransformer implements IClassTransformer {
    private static final String[] classesToTransform = {
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
        MethodNode originalIsOwnerMethod = null;
        for (MethodNode method : classOwnershipManager.methods) {
            if (method.name.equals("isOwner") && method.desc.equals("(Ljava/util/UUID;Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;)Z")) {
                originalIsOwnerMethod = method;
            }
        }

        if (originalIsOwnerMethod != null) {
            classOwnershipManager.methods.remove(originalIsOwnerMethod);
        }
        MethodVisitor mv = classOwnershipManager.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "isOwner", "(Ljava/util/UUID;Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;)Z", null, null);
        Label start = new Label();
        Label end = new Label();
        Label handler = new Label();
        Label lCatchBlockEnd = new Label();
        mv.visitCode();
        mv.visitTryCatchBlock(start, end, handler, "java/lang/NullPointerException");
        mv.visitLabel(start);
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(115, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/github/abrarsyed/secretroomsmod/common/OwnershipManager",
                "getOwner", "(Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;)Ljava/util/UUID;", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/github/abrarsyed/secretroomsmod/common/OwnershipManager",
                "equalsUUID", "(Ljava/util/UUID;Ljava/util/UUID;)Z", false);
        mv.visitInsn(Opcodes.IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("player", "Ljava/util/UUID;", null, l0, l1, 0);
        mv.visitLocalVariable("loc", "Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;", null, l0, l1, 1);
        mv.visitJumpInsn(Opcodes.GOTO, lCatchBlockEnd);
        mv.visitLabel(end);
        mv.visitLabel(handler);
        mv.visitLdcInsn("PEpakCoreTweaker:SR");
        mv.visitFieldInsn(Opcodes.GETSTATIC, "org/apache/logging/log4j/Level", "TRACE", "Lorg/apache/logging/log4j/Level;");
        mv.visitLdcInsn("Fix triggered, caught NullPointerException in com/github/abrarsyed/secretroomsmod/common/OwnershipManager.isOwner(Ljava/util/UUID;Lcom/github/abrarsyed/secretroomsmod/common/BlockLocation;)Z");
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cpw/mods/fml/relauncher/FMLRelaunchLog",
                "log", "(Ljava/lang/String;Lorg/apache/logging/log4j/Level;Ljava/lang/String;[Ljava/lang/Object;)V", false);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitLabel(lCatchBlockEnd);
        mv.visitMaxs(0,0);
        mv.visitEnd();
        log(Level.INFO, "   Rewrote method isOwner in class getOwnershipManager");
    }

    private void log(Level level, String message) {
        FMLRelaunchLog.log("PEpakCoreTweaker:SR", level, message);
    }
}
