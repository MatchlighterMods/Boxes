package ml.boxes.block;

import net.minecraft.util.Icon;

public enum MetaType {
	Crate("Boxes:crate", "Boxes.crate.name"),
	Safe("Boxes:safe", "Boxes.safe.name");
	
	public String ulName;
	public boolean hidden;
	public String icon;
	public Icon ricon;
	
	private MetaType(String ico, String ulName) {
		this(ico, ulName, false);
	}
	
	private MetaType(String ico, String ulName, boolean hide) {
		this.icon = ico;
		this.ulName = ulName;
		this.hidden = hide;
	}
	
	public static MetaType fromMeta(int meta){
		if (meta >-1 && meta<MetaType.values().length){
			return MetaType.values()[meta];
		}
		return null;
	}
}