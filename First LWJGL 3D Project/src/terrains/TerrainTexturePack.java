package terrains;

public class TerrainTexturePack {
	private TerrainTexture backgroundtexture;
	private TerrainTexture rtexture;
	private TerrainTexture gtexture;
	private TerrainTexture btexture;
	public TerrainTexturePack(TerrainTexture backgroundtexture, TerrainTexture rtexture, TerrainTexture gtexture,
			TerrainTexture btexture) {
		super();
		this.backgroundtexture = backgroundtexture;
		this.rtexture = rtexture;
		this.gtexture = gtexture;
		this.btexture = btexture;
	}
	
	public TerrainTexture getBackgroundTexture() {
		return backgroundtexture;
	}
	public void setBackgroundtexture(TerrainTexture backgroundtexture) {
		this.backgroundtexture = backgroundtexture;
	}
	public TerrainTexture getRTexture() {
		return rtexture;
	}
	public void setRtexture(TerrainTexture rtexture) {
		this.rtexture = rtexture;
	}
	public TerrainTexture getGTexture() {
		return gtexture;
	}
	public void setGtexture(TerrainTexture gtexture) {
		this.gtexture = gtexture;
	}
	public TerrainTexture getBTexture() {
		return btexture;
	}
	public void setBtexture(TerrainTexture btexture) {
		this.btexture = btexture;
	}
}