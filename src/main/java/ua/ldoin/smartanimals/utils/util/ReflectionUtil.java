package ua.ldoin.smartanimals.utils.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import ua.ldoin.smartanimals.utils.util.version.VersionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class ReflectionUtil {

    public static Class<?> getClass(@NotNull String className) throws ClassNotFoundException {

        return Class.forName(className);

    }

    public static Class<?> getNMSClass(@NotNull String className) throws ClassNotFoundException {

        return getClass("net.minecraft.server." + VersionManager.getVersion() + className);

    }

    public static Class<?> getCraftBukkitClass(@NotNull String className) throws ClassNotFoundException {

        return getClass("org.bukkit.craftbukkit." + VersionManager.getVersion() + className);

    }

    public static Class<?> getBukkitClass(@NotNull String className) throws ClassNotFoundException {

        return getClass("org.bukkit." + className);

    }

    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String methodName, Class<?>... args) throws NoSuchMethodException {

        return clazz.getMethod(methodName, args);

    }

    public static Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) throws NoSuchFieldException {

        return clazz.getDeclaredField(fieldName);

    }
}