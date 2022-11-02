package source.util;

public class Vector4 {

	public int x;
	public int y;
	public int z;
	public int w;

	public Vector4(int x) {
		this.x = x;
		this.z = x;
		this.y = x;
		this.w = x;
	}

	public Vector4(int x, int y) {
		this.x = x;
		this.z = x;
		this.y = y;
		this.w = y;
	}

	public Vector4(int x, int y, int z, int w) {
		this.x = x;
		this.z = y;
		this.y = z;
		this.w = w;
	}

}
