package ml.boxes.api;


public class BoxesApi {
	
	private static IBoxesApi boxesApi;
	
	public static IBoxesApi getBoxesApi() {
		if (boxesApi == null) {
			try {
				boxesApi = (IBoxesApi)Class.forName("ml.boxes.BoxesApi").getMethod("getInstance").invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return boxesApi;
	}
}
