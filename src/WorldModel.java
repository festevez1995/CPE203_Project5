import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import processing.core.PImage;

final class WorldModel {
    protected static final String QUAKE_ID = "quake";
    protected static final int QUAKE_ACTION_PERIOD = 1100;
    protected static final int QUAKE_ANIMATION_PERIOD = 100;
    protected static final int ORE_REACH = 1;
    protected static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    protected static final String BGND_KEY = "background";
    protected static final int BGND_NUM_PROPERTIES = 4;
    protected static final int BGND_ID = 1;
    protected static final int BGND_COL = 2;
    protected static final int BGND_ROW = 3;
    protected static final int PROPERTY_KEY = 0;
    protected static final String MINER_KEY = "miner";
    protected static final int MINER_NUM_PROPERTIES = 7;
    protected static final int MINER_ID = 1;
    protected static final int MINER_COL = 2;
    protected static final int MINER_ROW = 3;
    protected static final int MINER_LIMIT = 4;
    protected static final int MINER_ACTION_PERIOD = 5;
    protected static final int MINER_ANIMATION_PERIOD = 6;
    protected static final String OBSTACLE_KEY = "obstacle";
    protected static final int OBSTACLE_NUM_PROPERTIES = 4;
    protected static final int OBSTACLE_ID = 1;
    protected static final int OBSTACLE_COL = 2;
    protected static final int OBSTACLE_ROW = 3;
    protected static final String ORE_KEY = "ore";
    protected static final int ORE_NUM_PROPERTIES = 5;
    protected static final int ORE_ID = 1;
    protected static final int ORE_COL = 2;
    protected static final int ORE_ROW = 3;
    protected static final int ORE_ACTION_PERIOD = 4;
    protected static final String SMITH_KEY = "blacksmith";
    protected static final int SMITH_NUM_PROPERTIES = 4;
    protected static final int SMITH_ID = 1;
    protected static final int SMITH_COL = 2;
    protected static final int SMITH_ROW = 3;
    protected static final String VEIN_KEY = "vein";
    protected static final int VEIN_NUM_PROPERTIES = 5;
    protected static final int VEIN_ID = 1;
    protected static final int VEIN_COL = 2;
    protected static final int VEIN_ROW = 3;
    protected static final int VEIN_ACTION_PERIOD = 4;
    protected static final String BLOB_KEY = "blob";
    protected static final String BLOB_ID_SUFFIX = " -- blob";
    protected static final int BLOB_PERIOD_SCALE = 4;
    protected static final int BLOB_ANIMATION_MIN = 50;
    protected static final int BLOB_ANIMATION_MAX = 150;
    protected static final String QUAKE_KEY = "quake";
    protected static final String ORE_ID_PREFIX = "ore -- ";
    protected static final int ORE_CORRUPT_MIN = 20000;
    protected static final int ORE_CORRUPT_MAX = 30000;
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;
    //public static final int ORE_REACH = 1;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    /*
     * Assumes that there is no entity currently occupying the intended destination
     * cell.
     */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /*
             * this moves the entity just outside of the grid for debugging purposes
             */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<Entity> findNearest(Point pos, Class kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getClass() == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = pos.distanceSquared(nearest.getPosition());

            for (Entity other : entities) {
                int otherDistance = pos.distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (withinBounds(newPt) && !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public Entity getOccupancyCell(Point p) {
        return occupancy[p.y][p.x];
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public void setOccupancyCell(Point p, Entity entity) {
        occupancy[p.y][p.x] = entity;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0 && pos.x < numCols;
    }


//	public Point nextPositionMiner(Entity entity, Point destPos) {
//		int horiz = Integer.signum(destPos.x - entity.getPosition().x);
//		Point newPos = new Point(entity.getPosition().x + horiz, entity.getPosition().y);
//
//		if (horiz == 0 || isOccupied(newPos)) {
//			int vert = Integer.signum(destPos.y - entity.getPosition().y);
//			newPos = new Point(entity.getPosition().x, entity.getPosition().y + vert);
//
//			if (vert == 0 || isOccupied(newPos)) {
//				newPos = entity.getPosition();
//			}
//		}
//
//		return newPos;
//	}
//
//	public Point nextPositionOreBlob(Entity entity, Point destPos) {
//		int horiz = Integer.signum(destPos.x - entity.getPosition().x);
//		Point newPos = new Point(entity.getPosition().x + horiz, entity.getPosition().y);
//
//		Optional<Entity> occupant = getOccupant(newPos);
//
//		if (horiz == 0 || (occupant.isPresent() && !(occupant.get() instanceof Ore))) {
//			int vert = Integer.signum(destPos.y - entity.getPosition().y);
//			newPos = new Point(entity.getPosition().x, entity.getPosition().y + vert);
//			occupant = getOccupant(newPos);
//
//			if (vert == 0 || (occupant.isPresent() && !(occupant.get() instanceof Ore))) {
//				newPos = entity.getPosition();
//			}
//		}
//
//		return newPos;
//	}


    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background) {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }


    public void load(Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d", lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s", lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    public boolean processLine(String line, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, imageStore);
                case MINER_KEY:
                    return parseMiner(properties, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case ORE_KEY:
                    return parseOre(properties, imageStore);
                case SMITH_KEY:
                    return parseSmith(properties, imageStore);
                case VEIN_KEY:
                    return parseVein(properties, imageStore);
            }
        }

        return false;
    }

    public boolean parseBackground(String[] properties, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]), Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }


    public boolean parseMiner(String[] properties, ImageStore imageStore) {//miner not full
        if (properties.length == MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]), Integer.parseInt(properties[MINER_ROW]));
            Entity entity = MinerNotFull.createMinerNotFull(properties[MINER_ID], pt, imageStore.getImageList(MINER_KEY), Integer.parseInt(properties[MINER_LIMIT]), 0,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]));
            tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    public boolean parseObstacle(String[] properties, ImageStore imageStore) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID], pt, imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseOre(String[] properties, ImageStore imageStore) {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]), Integer.parseInt(properties[ORE_ROW]));
            Entity entity = Ore.createOre(properties[ORE_ID], pt, imageStore.getImageList(ORE_KEY), 0, 0, Integer.parseInt(properties[ORE_ACTION_PERIOD]));
            tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    public boolean parseSmith(String[] properties, ImageStore imageStore) {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]), Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = Blacksmith.createBlacksmith(properties[SMITH_ID], pt, imageStore.getImageList(SMITH_KEY));
            tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    public boolean parseVein(String[] properties, ImageStore imageStore) {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]), Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = Vein.createVein(properties[VEIN_ID], pt, imageStore.getImageList(VEIN_KEY), 0, 0, Integer.parseInt(properties[VEIN_ACTION_PERIOD]));
            tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public int gteNumRows() {
        return this.numRows;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
