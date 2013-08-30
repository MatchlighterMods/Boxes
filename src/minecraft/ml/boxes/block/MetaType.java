package ml.boxes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Icon;

public enum MetaType {
	Crate(0),
	Safe(1),
	DisplayCase(2);
	
	public final int meta;
	public final int metaLength;
	
	public List<Icon> icons = new ArrayList<Icon>();
	
	private MetaType(int meta) {
		this(meta, 1);
	}
	
	private MetaType(int meta, int mLength) {
		this.meta = meta;
		this.metaLength = mLength;
	}
	
	public boolean hidden() {
		return this.getClass().getAnnotation(Hide.class) != null;
	}
	
	public static MetaType fromMeta(int meta){
		for (MetaType mt : values()) {
			if (mt.meta >= meta && meta<mt.meta+mt.metaLength) {
				return mt;
			}
		}
		
		return null;
	}
	
	private static @interface Hide {}
}