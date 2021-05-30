package cn.nukkit.utils;

/**
 * @author GoodLucky777
 */
public class Identifier {

    private static final String SEPARATOR = ":";
    
    private final String namespace;
    private final String name;
    
    public Identifier(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }
    
    public String getIdentifier() {
        if (namespace.equals("minecraft")) {
            return name;
        }
        return this.getFullIdentifier();
    }
    
    public String getFullIdentifier() {
        return namespace + SEPARATOR + name;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public static String getSeperator() {
        return SEPARATOR;
    }
}
