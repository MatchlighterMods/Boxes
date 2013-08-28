package ml.boxes.tile;

import ml.core.block.BlockUtils;
import ml.core.vec.Vector3;
import ml.core.vec.transform.Matrix4d;
import ml.core.vec.transform.Rotation;
import ml.core.vec.transform.Scale;
import ml.core.vec.transform.Transformation;
import ml.core.vec.transform.Translation;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityDisplayCase extends TileEntity {

	public ForgeDirection facing = ForgeDirection.UP;
	public ForgeDirection rotation = ForgeDirection.NORTH;
	
	public TileEntityDisplayCase() {
		
	}
	
	public Transformation getTransformation() {
		Transformation t = new Rotation(new Vector3(0,1,0), BlockUtils.getRotationFromDirection(rotation));
		if (facing != ForgeDirection.UP) {
			t = t.append(new Rotation(new Vector3(1, 0, 0), -90));
			t = t.append(new Rotation(new Vector3(0,1,0), BlockUtils.getRotationFromDirection(facing)));
		}
		t = t.localize(new Vector3(0.5D, 0.5D, 0.5D));
		
		return t;
	}
	
}
