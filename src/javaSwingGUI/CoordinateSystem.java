package javaSwingGUI;

import gameElements.out_of_classroom.entities.gamelogic.Cords;
import gameElements.out_of_classroom.io.Vector2D;

public class CoordinateSystem {
    private double cameraX;
    private double cameraY;
    private int tileSize;
    
    private final GlobalCoordinate tempCoordinate = new GlobalCoordinate(0, 0);
    
    public CoordinateSystem() {
        this(0, 0, GlobalCoordinate.DEFAULT_TILE_SIZE);
    }
    
    public CoordinateSystem(double cameraX, double cameraY, int tileSize) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.tileSize = tileSize;
    }
    
    public void setCameraPosition(double cameraX, double cameraY) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }
    
    public void setCameraX(double cameraX) {
        this.cameraX = cameraX;
    }
    
    public void setCameraY(double cameraY) {
        this.cameraY = cameraY;
    }
    
    public double getCameraX() {
        return cameraX;
    }
    
    public double getCameraY() {
        return cameraY;
    }
    
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public GlobalCoordinate worldToScreen(GlobalCoordinate worldCoord) {
        return new GlobalCoordinate(worldCoord.getWorldX() - cameraX, worldCoord.getWorldY() - cameraY);
    }
    
    public GlobalCoordinate screenToWorld(GlobalCoordinate screenCoord) {
        return new GlobalCoordinate(screenCoord.getWorldX() + cameraX, screenCoord.getWorldY() + cameraY);
    }
    
    public GlobalCoordinate worldToScreen(double worldX, double worldY) {
        return new GlobalCoordinate(worldX - cameraX, worldY - cameraY);
    }
    
    public GlobalCoordinate screenToWorld(double screenX, double screenY) {
        return new GlobalCoordinate(screenX + cameraX, screenY + cameraY);
    }
    
    public GlobalCoordinate worldToScreenFast(GlobalCoordinate worldCoord) {
        tempCoordinate.setValues(worldCoord.getWorldX() - cameraX, worldCoord.getWorldY() - cameraY);
        return tempCoordinate;
    }
    
    public GlobalCoordinate screenToWorldFast(GlobalCoordinate screenCoord) {
        tempCoordinate.setValues(screenCoord.getWorldX() + cameraX, screenCoord.getWorldY() + cameraY);
        return tempCoordinate;
    }
    
    public GlobalCoordinate worldToScreenFast(double worldX, double worldY) {
        tempCoordinate.setValues(worldX - cameraX, worldY - cameraY);
        return tempCoordinate;
    }
    
    public GlobalCoordinate screenToWorldFast(double screenX, double screenY) {
        tempCoordinate.setValues(screenX + cameraX, screenY + cameraY);
        return tempCoordinate;
    }
    
    public void transformWorldToScreen(GlobalCoordinate coordinate) {
        coordinate.transformToScreen(cameraX, cameraY);
    }
    
    public void transformScreenToWorld(GlobalCoordinate coordinate) {
        coordinate.transformToWorld(cameraX, cameraY);
    }
    
    public GlobalCoordinate.TileCoordinate worldToTile(GlobalCoordinate worldCoord) {
        return worldCoord.getTileCoordinates(tileSize);
    }
    
    public GlobalCoordinate tileToWorld(GlobalCoordinate.TileCoordinate tileCoord) {
        return tileCoord.toWorldCoordinates(tileSize);
    }
    
    public GlobalCoordinate tileToWorld(int tileX, int tileY) {
        return GlobalCoordinate.fromTileCoordinates(tileX, tileY, tileSize);
    }
    
    public GlobalCoordinate screenToTileWorld(double screenX, double screenY) {
        GlobalCoordinate worldCoord = screenToWorld(screenX, screenY);
        GlobalCoordinate.TileCoordinate tileCoord = worldToTile(worldCoord);
        return tileToWorld(tileCoord);
    }
    
    public boolean isOnScreen(GlobalCoordinate worldCoord, double screenWidth, double screenHeight) {
        double screenX = worldCoord.getWorldX() - cameraX;
        double screenY = worldCoord.getWorldY() - cameraY;
        return screenX >= 0 && screenX <= screenWidth && screenY >= 0 && screenY <= screenHeight;
    }
    
    public boolean isOnScreenFast(double worldX, double worldY, double screenWidth, double screenHeight) {
        double screenX = worldX - cameraX;
        double screenY = worldY - cameraY;
        return screenX >= 0 && screenX <= screenWidth && screenY >= 0 && screenY <= screenHeight;
    }
    
    public GlobalCoordinate clampToScreen(GlobalCoordinate worldCoord, double screenWidth, double screenHeight) {
        GlobalCoordinate screenCoord = worldToScreen(worldCoord);
        double clampedX = Math.max(0, Math.min(screenWidth, screenCoord.getWorldX()));
        double clampedY = Math.max(0, Math.min(screenHeight, screenCoord.getWorldY()));
        return screenToWorld(clampedX, clampedY);
    }
    
    public static GlobalCoordinate convertFromLegacyCords(Cords cords) {
        return GlobalCoordinate.fromCords(cords);
    }
    
    public static GlobalCoordinate convertFromLegacyVector(Vector2D vector) {
        return GlobalCoordinate.fromVector(vector);
    }
    
    public static Cords convertToLegacyCords(GlobalCoordinate coord) {
        return coord.toCords();
    }
    
    public static Vector2D convertToLegacyVector(GlobalCoordinate coord) {
        return coord.toVector();
    }
}