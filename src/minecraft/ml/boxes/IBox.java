package ml.boxes;

public interface IBox {

	public void saveData();
	
	public void boxOpen();
	
	public void boxClose();
	
	public BoxData getBoxData();
}
