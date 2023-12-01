package tiki.advancedlootabletweaker;

public class ModInfo {
	public static final String ID = "advancedlootabletweaker";
	public static final String NAME = "Advanced Lootable Tweaker";
	public static final String VER = "1.0.0-1.12.2";
	public static final String LOAD_AFTER = "required-after:FML;required-after:advancedlootableweapons@[1.4.0,);required-before:contenttweaker@[1.12.2-4.9.0,);before:crafttweaker;";
	public static final String CLIENTPROXY = "tiki.advancedlootabletweaker.proxy.ClientProxy";
	public static final String COMMONPROXY = "tiki.advancedlootabletweaker.proxy.CommonProxy";
}
