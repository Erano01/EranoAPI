import java.lang.reflect.*;

/** Prints org.bukkit.Material of the API jar on the classpath: name, id, legacy, block, item, deprecated. */
public class Dump {
    public static void main(String[] args) throws Exception {
        Class<?> type = Class.forName("org.bukkit.Material");
        System.out.println("#enum=" + type.isEnum() + " interface=" + type.isInterface());
        Object[] values = type.isEnum() ? type.getEnumConstants() : null;
        if (values == null) {
            for (Field f : type.getFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getType() == type) {
                    System.out.println(f.getName() + "\t\t\t\t\t" + (f.isAnnotationPresent(Deprecated.class) ? 1 : 0));
                }
            }
            return;
        }
        for (Object value : values) {
            String name = ((Enum<?>) value).name();
            Field field = type.getField(name);
            System.out.println(name + "\t" + call(value, "getId") + "\t" + call(value, "isLegacy") + "\t"
                    + call(value, "isBlock") + "\t" + call(value, "isItem") + "\t"
                    + (field.isAnnotationPresent(Deprecated.class) ? 1 : 0));
        }
    }

    static String call(Object target, String method) {
        try {
            Object r = target.getClass().getMethod(method).invoke(target);
            return r instanceof Boolean ? ((Boolean) r ? "1" : "0") : String.valueOf(r);
        } catch (Throwable e) {
            return "";
        }
    }
}
