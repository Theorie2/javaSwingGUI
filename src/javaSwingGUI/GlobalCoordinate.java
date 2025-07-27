package javaSwingGUI;

import gameElements.out_of_classroom.entities.gamelogic.Cords;
import gameElements.out_of_classroom.io.Vector2D;

public class GlobalCoordinate {
    private double worldX;
    private double worldY;
    
    public static final int DEFAULT_TILE_SIZE = 64;
    
    public GlobalCoordinate(double worldX, double worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
    }
    
    public static GlobalCoordinate of(double x, double y) {
        return new GlobalCoordinate(x, y);
    }
    
    public static GlobalCoordinate fromCords(Cords cords) {
        return new GlobalCoordinate(cords.x(), cords.y());
    }
    
    public static GlobalCoordinate fromVector(Vector2D vector) {
        return new GlobalCoordinate(vector.x, vector.y);
    }
    
    public static GlobalCoordinate fromScreenCoordinates(double screenX, double screenY, double cameraX, double cameraY) {
        return new GlobalCoordinate(screenX + cameraX, screenY + cameraY);
    }
    
    public static GlobalCoordinate fromTileCoordinates(int tileX, int tileY, int tileSize) {
        return new GlobalCoordinate(tileX * tileSize + tileSize / 2.0, tileY * tileSize + tileSize / 2.0);
    }
    
    public static GlobalCoordinate fromTileCoordinates(int tileX, int tileY) {
        return fromTileCoordinates(tileX, tileY, DEFAULT_TILE_SIZE);
    }
    
    public double getWorldX() {
        return worldX;
    }
    
    public double getWorldY() {
        return worldY;
    }
    
    public void setWorldX(double worldX) {
        this.worldX = worldX;
    }
    
    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }
    
    public void set(double worldX, double worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
    }
    
    public void set(GlobalCoordinate other) {
        this.worldX = other.worldX;
        this.worldY = other.worldY;
    }
    
    public GlobalCoordinate getScreenCoordinates(double cameraX, double cameraY) {
        return new GlobalCoordinate(worldX - cameraX, worldY - cameraY);
    }
    
    public TileCoordinate getTileCoordinates(int tileSize) {
        int tileX = (int) Math.floor(worldX / tileSize);
        int tileY = (int) Math.floor(worldY / tileSize);
        return new TileCoordinate(tileX, tileY);
    }
    
    public TileCoordinate getTileCoordinates() {
        return getTileCoordinates(DEFAULT_TILE_SIZE);
    }
    
    public GlobalCoordinate add(Vector2D vector) {
        return new GlobalCoordinate(worldX + vector.x, worldY + vector.y);
    }
    
    public GlobalCoordinate add(GlobalCoordinate other) {
        return new GlobalCoordinate(worldX + other.worldX, worldY + other.worldY);
    }
    
    public GlobalCoordinate add(double deltaX, double deltaY) {
        return new GlobalCoordinate(worldX + deltaX, worldY + deltaY);
    }
    
    public GlobalCoordinate subtract(GlobalCoordinate other) {
        return new GlobalCoordinate(worldX - other.worldX, worldY - other.worldY);
    }
    
    public GlobalCoordinate multiply(double scalar) {
        return new GlobalCoordinate(worldX * scalar, worldY * scalar);
    }
    
    public GlobalCoordinate addMutable(double deltaX, double deltaY) {
        this.worldX += deltaX;
        this.worldY += deltaY;
        return this;
    }
    
    public GlobalCoordinate addMutable(Vector2D vector) {
        this.worldX += vector.x;
        this.worldY += vector.y;
        return this;
    }
    
    public GlobalCoordinate addMutable(GlobalCoordinate other) {
        this.worldX += other.worldX;
        this.worldY += other.worldY;
        return this;
    }
    
    public GlobalCoordinate subtractMutable(GlobalCoordinate other) {
        this.worldX -= other.worldX;
        this.worldY -= other.worldY;
        return this;
    }
    
    public GlobalCoordinate multiplyMutable(double scalar) {
        this.worldX *= scalar;
        this.worldY *= scalar;
        return this;
    }
    
    public GlobalCoordinate setValues(double x, double y) {
        this.worldX = x;
        this.worldY = y;
        return this;
    }
    
    public void transformToScreen(double cameraX, double cameraY) {
        this.worldX -= cameraX;
        this.worldY -= cameraY;
    }
    
    public void transformToWorld(double cameraX, double cameraY) {
        this.worldX += cameraX;
        this.worldY += cameraY;
    }
    
    public double distanceTo(GlobalCoordinate other) {
        double dx = worldX - other.worldX;
        double dy = worldY - other.worldY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double distanceSquaredTo(GlobalCoordinate other) {
        double dx = worldX - other.worldX;
        double dy = worldY - other.worldY;
        return dx * dx + dy * dy;
    }
    
    public double fastDistanceTo(double otherX, double otherY) {
        double dx = worldX - otherX;
        double dy = worldY - otherY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double fastDistanceSquaredTo(double otherX, double otherY) {
        double dx = worldX - otherX;
        double dy = worldY - otherY;
        return dx * dx + dy * dy;
    }
    
    public double manhattanDistanceTo(double otherX, double otherY) {
        return Math.abs(worldX - otherX) + Math.abs(worldY - otherY);
    }
    
    public double manhattanDistanceTo(GlobalCoordinate other) {
        return Math.abs(worldX - other.worldX) + Math.abs(worldY - other.worldY);
    }
    
    public Vector2D vectorTo(GlobalCoordinate other) {
        return new Vector2D(other.worldX - worldX, other.worldY - worldY);
    }
    
    public Vector2D toVector() {
        return new Vector2D(worldX, worldY);
    }
    
    public Cords toCords() {
        return new Cords(worldX, worldY);
    }
    
    public GlobalCoordinate copy() {
        return new GlobalCoordinate(worldX, worldY);
    }
    
    public void copyFrom(GlobalCoordinate other) {
        this.worldX = other.worldX;
        this.worldY = other.worldY;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GlobalCoordinate that = (GlobalCoordinate) obj;
        return Double.compare(that.worldX, worldX) == 0 && Double.compare(that.worldY, worldY) == 0;
    }
    
    public int hashCode() {
        return Double.hashCode(worldX) * 31 + Double.hashCode(worldY);
    }
    
    @Override
    public String toString() {
        return String.format("GlobalCoordinate(%.2f, %.2f)", worldX, worldY);
    }
    
    public static class TileCoordinate {
        public final int x;
        public final int y;
        
        public TileCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public GlobalCoordinate toWorldCoordinates(int tileSize) {
            return GlobalCoordinate.fromTileCoordinates(x, y, tileSize);
        }
        
        public GlobalCoordinate toWorldCoordinates() {
            return toWorldCoordinates(DEFAULT_TILE_SIZE);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TileCoordinate that = (TileCoordinate) obj;
            return x == that.x && y == that.y;
        }
        
        @Override
        public int hashCode() {
            return x * 31 + y;
        }
        
        @Override
        public String toString() {
            return String.format("TileCoordinate(%d, %d)", x, y);
        }
    }
}